package com.sluv.api.celeb.service;

import com.sluv.api.celeb.dto.response.CelebChipResponse;
import com.sluv.api.celeb.dto.response.CelebSearchByCategoryResponse;
import com.sluv.api.celeb.dto.response.CelebSearchResponse;
import com.sluv.api.celeb.dto.response.RecentSelectCelebResponse;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.CelebCategory;
import com.sluv.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.domain.celeb.service.CelebCategoryDomainService;
import com.sluv.domain.celeb.service.CelebDomainService;
import com.sluv.domain.celeb.service.RecentSelectCelebDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CelebService {

    private final UserDomainService userDomainService;
    private final CelebDomainService celebDomainService;
    private final CelebCategoryDomainService celebCategoryDomainService;
    private final RecentSelectCelebDomainService recentSelectCelebDomainService;

    @Transactional(readOnly = true)
    public PaginationResponse<CelebSearchResponse> searchCeleb(String celebName, Pageable pageable) {
        Page<Celeb> celebPage = celebDomainService.searchCeleb(celebName, pageable);

        List<CelebSearchResponse> content = celebPage.stream()
                .map(CelebSearchResponse::of)
                .toList();

        return PaginationResponse.create(celebPage, content);

    }

    @Transactional(readOnly = true)
    public List<RecentSelectCelebResponse> getUserRecentSelectCeleb(Long userId) {
        User user = userDomainService.findById(userId);
        List<RecentSelectCeleb> recentSelectCelebList = recentSelectCelebDomainService.getRecentSelectCelebTop20(user);

        return recentSelectCelebList
                .stream()
                .map(RecentSelectCelebResponse::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CelebSearchResponse> getTop10Celeb() {
        return celebDomainService.findTop10Celeb()
                .stream()
                .map(CelebSearchResponse::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CelebSearchByCategoryResponse> getCelebByCategory() {
        // Parent Id가 null인 CelebCategory를 모두 조회
        List<CelebCategory> categoryList = celebCategoryDomainService.findAllByParentIdIsNull();
        changeCategoryOrder(categoryList);

        return categoryList.stream()
                // 카테고리별 CelebSearchByCategoryResDto 생성
                .map(category -> CelebSearchByCategoryResponse.of(category,
                                celebDomainService.getCelebByCategory(category)
                                        .stream()
                                        // 셀럽별 CelebChipResDto 생성
                                        .map(CelebChipResponse::of)
                                        .toList()
                        )
                ).toList();

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

    @Transactional(readOnly = true)
    public List<CelebSearchByCategoryResponse> searchInterestedCelebByName(String celebName) {
        // 1. Parent Celeb과 일치
        List<Celeb> celebByParent = celebDomainService.searchInterestedCelebByParent(celebName);

        // 2. Child Celeb과 일치
        List<Celeb> celebByChild = celebDomainService.searchInterestedCelebByChild(celebName);

        // 1 + 2 를 합친 Celeb
        List<Celeb> celebList = Stream.concat(celebByParent.stream(), celebByChild.stream()).distinct().toList();

        // Celeb의 모든 카테고리 조회
        List<CelebCategory> celebCategoryList = celebCategoryDomainService.findAllByParentIdIsNull();

        // Category별 맞는 celeb들을 골라내서 조립.
        return celebCategoryList.stream()
                // 카테고리별 분류
                .map(category -> {
                    List<CelebChipResponse> eachCategoryCeleb =
                            // 카테고리에 맞는 셀럽 filtering
                            celebList.stream().filter(celeb -> {
                                        // ParentCategory가 있다면 ParentCategory. 없다면, CelebCategory.
                                        CelebCategory tempCategory = celeb.getCelebCategory().getParent() != null
                                                ? celeb.getCelebCategory().getParent()
                                                : celeb.getCelebCategory();

                                        return tempCategory == category;
                                        // Category에 맞게 분류된 celeb을 Dto로 변경
                                    }).map(CelebChipResponse::of)
                                    // 가나다 순으로 정렬
                                    .sorted(Comparator.comparing(CelebChipResponse::getCelebName))
                                    .toList();

                    return CelebSearchByCategoryResponse.of(category, eachCategoryCeleb);
                })
                .toList();
    }

}
