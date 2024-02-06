package com.sluv.server.domain.user.service;

import com.sluv.server.domain.celeb.dto.InterestedCelebCategoryResDto;
import com.sluv.server.domain.celeb.dto.InterestedCelebParentResDto;
import com.sluv.server.domain.celeb.dto.InterestedCelebPostReqDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.CelebCategory;
import com.sluv.server.domain.celeb.entity.InterestedCeleb;
import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.celeb.repository.CelebCategoryRepository;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.InterestedCelebRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.enums.UserStatus;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCelebService {

    private final UserRepository userRepository;
    private final CelebRepository celebRepository;
    private final CelebCategoryRepository celebCategoryRepository;
    private final InterestedCelebRepository interestedCelebRepository;

    /**
     * User가 선택한 관심 셀럽을 검색 관심 샐럼의 상위 카테고리를 기준으로 묶어서 Response
     */
    @Transactional(readOnly = true)
    public List<InterestedCelebCategoryResDto> getInterestedCelebByCategory(User user) {
        List<Celeb> interestedCelebList = celebRepository.findInterestedCeleb(user);

        List<CelebCategory> categoryList = celebCategoryRepository.findAllByParentIdIsNull();
        changeCategoryOrder(categoryList);

        return categoryList.stream()
                .map(category -> { // 카테고리별 InterestedCelebCategoryResDto 생성
                    List<Celeb> categoryFilterCeleb = getCategoryFilterCeleb(interestedCelebList, category);
                    return InterestedCelebCategoryResDto.of(category,
                            convertInterestedCelebParentResDto(categoryFilterCeleb));
                }).toList();
    }

    /**
     * 특정 유저가 선택한 관심 Celeb을 조회 CelebCategory를 기준으로 그룹핑 유저가 등록한 순서대로 조회
     */
    @Transactional(readOnly = true)
    public List<InterestedCelebParentResDto> getTargetUserInterestedCelebByPostTime(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return celebRepository.findInterestedCeleb(user)
                .stream()
                .map(InterestedCelebParentResDto::of)
                .toList();
    }

    /**
     * User가 선택한 관심 셀럽을 검색 관심 셀럽 선택순서를 기준으로 조회
     */
    @Transactional(readOnly = true)
    public List<InterestedCelebParentResDto> getInterestedCelebByPostTime(User user) {
        return celebRepository.findInterestedCeleb(user)
                .stream()
                .map(InterestedCelebParentResDto::of)
                .toList();
    }

    @Transactional
    public void postInterestedCeleb(User user, InterestedCelebPostReqDto dto) {
        // 기존에 있는 해당 유저의 관심셀럽 초기화
        if (user.getUserStatus().equals(UserStatus.ACTIVE)) {
            interestedCelebRepository.deleteAllByUserId(user.getId());
        }

        // 초기화 상태에서 다시 추가.
        List<InterestedCeleb> interestedCelebList = dto.getCelebIdList().stream()
                .map(celeb -> InterestedCeleb.toEntity(user,
                        celebRepository.findById(celeb).orElseThrow(CelebNotFoundException::new))).toList();

        interestedCelebRepository.saveAll(interestedCelebList);

        if (user.getUserStatus().equals(UserStatus.PENDING_CELEB)) {
            user.changeUserStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
    }

    /**
     * 특정 유저가 선택한 관심 Celeb을 조회 CelebCategory를 기준으로 그룹핑 카테고리를 기준으로 조회
     */
    @Transactional(readOnly = true)
    public List<InterestedCelebCategoryResDto> getTargetUserInterestedCelebByCategory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Celeb> interestedCelebList = celebRepository.findInterestedCeleb(user);

        List<CelebCategory> categoryList = celebCategoryRepository.findAllByParentIdIsNull();
        categoryList.sort(Comparator.comparing(CelebCategory::getName));
        changeCategoryOrder(categoryList);

        return categoryList.stream()
                // 카테고리별 InterestedCelebCategoryResDto 생성
                .map(category -> {
                    List<Celeb> categoryFilterCeleb = getCategoryFilterCeleb(interestedCelebList, category);
                    return InterestedCelebCategoryResDto.of(category,
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
    private List<InterestedCelebParentResDto> convertInterestedCelebParentResDto(List<Celeb> celebList) {
        return celebList.stream().map(InterestedCelebParentResDto::of).toList();
    }
}
