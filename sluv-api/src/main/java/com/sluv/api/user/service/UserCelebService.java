package com.sluv.api.user.service;

import com.sluv.api.celeb.dto.request.InterestedCelebPostRequest;
import com.sluv.api.celeb.dto.response.InterestedCelebCategoryResponse;
import com.sluv.api.celeb.dto.response.InterestedCelebParentResponse;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.CelebCategory;
import com.sluv.domain.celeb.entity.InterestedCeleb;
import com.sluv.domain.celeb.service.CelebCategoryDomainService;
import com.sluv.domain.celeb.service.CelebDomainService;
import com.sluv.domain.celeb.service.InterestedCelebDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserStatus;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.discord.WebHookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCelebService {

    private final UserDomainService userDomainService;
    private final CelebDomainService celebDomainService;
    private final CelebCategoryDomainService celebCategoryDomainService;
    private final InterestedCelebDomainService interestedCelebDomainService;

    private final WebHookService webHookService;

    /**
     * User가 선택한 관심 셀럽을 검색 관심 샐럼의 상위 카테고리를 기준으로 묶어서 Response
     */
    @Transactional(readOnly = true)
    public List<InterestedCelebCategoryResponse> getInterestedCelebByCategory(Long userId) {
        User user = userDomainService.findByIdOrNull(userId);
        List<Celeb> interestedCelebList;
        if (user != null) {
            interestedCelebList = celebDomainService.findInterestedCeleb(user);
        } else {
            interestedCelebList = celebDomainService.findTop10Celeb();
        }

        List<CelebCategory> categoryList = celebCategoryDomainService.findAllByParentIdIsNull();
        changeCategoryOrder(categoryList);

        return categoryList.stream()
                .map(category -> { // 카테고리별 InterestedCelebCategoryResDto 생성
                    List<Celeb> categoryFilterCeleb = getCategoryFilterCeleb(interestedCelebList, category);
                    return InterestedCelebCategoryResponse.of(category,
                            convertInterestedCelebParentResDto(categoryFilterCeleb));
                }).toList();
    }

    /**
     * 특정 유저가 선택한 관심 Celeb을 조회 CelebCategory를 기준으로 그룹핑 유저가 등록한 순서대로 조회
     */
    @Transactional(readOnly = true)
    public List<InterestedCelebParentResponse> getTargetUserInterestedCelebByPostTime(Long userId) {
        User user = userDomainService.findById(userId);

        return celebDomainService.findInterestedCeleb(user)
                .stream()
                .map(InterestedCelebParentResponse::of)
                .toList();
    }

    /**
     * User가 선택한 관심 셀럽을 검색 관심 셀럽 선택순서를 기준으로 조회
     */
    @Transactional(readOnly = true)
    public List<InterestedCelebParentResponse> getInterestedCelebByPostTime(Long userId) {
        User user = userDomainService.findByIdOrNull(userId);
        List<InterestedCelebParentResponse> dtos;
        if (user != null) {
            dtos = celebDomainService.findInterestedCeleb(user)
                    .stream()
                    .map(InterestedCelebParentResponse::of)
                    .toList();
        } else {
            dtos = celebDomainService.findTop10Celeb()
                    .stream()
                    .map(celeb -> {
                        if (celeb.getParent() != null) {
                            celeb = celeb.getParent();
                        }
                        return InterestedCelebParentResponse.of(celeb);
                    })
                    .distinct()
                    .toList();
        }
        return dtos;
    }

    @Transactional
    public void postInterestedCeleb(Long userId, InterestedCelebPostRequest dto) {
        User user = userDomainService.findById(userId);
        // 기존에 있는 해당 유저의 관심셀럽 초기화
        if (user.getUserStatus().equals(UserStatus.ACTIVE)) {
            interestedCelebDomainService.deleteAllByUserId(user.getId());
        }

        // 초기화 상태에서 다시 추가.
        List<InterestedCeleb> interestedCelebList = dto.getCelebIdList().stream()
                .map(celeb -> InterestedCeleb.toEntity(user,
                        celebDomainService.findById(celeb))).toList();

        interestedCelebDomainService.saveAll(interestedCelebList);

        if (user.getUserStatus().equals(UserStatus.PENDING_CELEB)) {
            user.changeUserStatus(UserStatus.ACTIVE);
            userDomainService.saveUser(user);
            webHookService.sendSingupMessage(user);
        }
    }

    /**
     * 특정 유저가 선택한 관심 Celeb을 조회 CelebCategory를 기준으로 그룹핑 카테고리를 기준으로 조회
     */
    @Transactional(readOnly = true)
    public List<InterestedCelebCategoryResponse> getTargetUserInterestedCelebByCategory(Long userId) {
        User user = userDomainService.findById(userId);
        List<Celeb> interestedCelebList = celebDomainService.findInterestedCeleb(user);

        List<CelebCategory> categoryList = celebCategoryDomainService.findAllByParentIdIsNull();
        categoryList.sort(Comparator.comparing(CelebCategory::getName));
        changeCategoryOrder(categoryList);

        return categoryList.stream()
                // 카테고리별 InterestedCelebCategoryResDto 생성
                .map(category -> {
                    List<Celeb> categoryFilterCeleb = getCategoryFilterCeleb(interestedCelebList, category);
                    return InterestedCelebCategoryResponse.of(category,
                            convertInterestedCelebParentResDto(categoryFilterCeleb));
                }).toList();
    }

    /**
     * 가수 -> 배우 -> 방송인 -> 스포츠인 -> 인플루언서 순서로 변
     */

    private void changeCategoryOrder(List<CelebCategory> categoryList) {
        categoryList.sort(Comparator.comparing(CelebCategory::getName));

        CelebCategory tempCategory = categoryList.get(1);
        categoryList.set(1, categoryList.get(2));
        categoryList.set(2, tempCategory);
    }

    /**
     * 관심셀럽 목록에서 category와 일치하는 Celeb을 분류
     */
    private List<Celeb> getCategoryFilterCeleb(List<Celeb> celebList, CelebCategory category) {
        return celebList.stream().filter(celeb ->
                // interestedCeleb의 상위 카테고리 id와 카테고리별 묶을 카테고리의 아이디가 일치하는 것만 filtering
                Objects.equals(
                        celeb.getCelebCategory().getParent() != null ? celeb.getCelebCategory().getParent().getId()
                                : celeb.getCelebCategory().getId(), category.getId())).toList();
    }

    /**
     * 관심셀럽 목록에서 category와 일치하는 Celeb을 분류
     */
    private List<InterestedCelebParentResponse> convertInterestedCelebParentResDto(List<Celeb> celebList) {
        return celebList.stream().map(InterestedCelebParentResponse::of).toList();
    }
}
