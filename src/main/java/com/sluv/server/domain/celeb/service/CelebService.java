package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.celeb.dto.CelebChipResDto;
import com.sluv.server.domain.celeb.dto.CelebSearchByCategoryResDto;
import com.sluv.server.domain.celeb.dto.CelebSearchResDto;
import com.sluv.server.domain.celeb.dto.RecentSelectCelebResDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.CelebCategory;
import com.sluv.server.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.server.domain.celeb.repository.CelebCategoryRepository;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.RecentSelectCelebRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CelebService {

    private final CelebRepository celebRepository;
    private final RecentSelectCelebRepository recentSearchCelebRepository;
    private final CelebCategoryRepository celebCategoryRepository;

    @Transactional(readOnly = true)
    public PaginationResDto<CelebSearchResDto> searchCeleb(String celebName, Pageable pageable) {

        Page<Celeb> celebPage = celebRepository.searchCeleb(celebName, pageable);

        List<Celeb> childCelebList = celebPage.stream()
                .filter(celeb -> celeb.getParent() != null)
                .toList();

        Stream<CelebSearchResDto> childCelebDtoStream = changeCelebSearchResDto(childCelebList).stream();

        List<Celeb> parentCelebList = celebPage.stream()
                .filter(celeb -> celeb.getParent() == null)
                .toList();
        Stream<CelebSearchResDto> parentCelebDtoStream = changeCelebSearchResDto(parentCelebList).stream();

        return PaginationResDto.of(celebPage,
                Stream.concat(childCelebDtoStream, parentCelebDtoStream)
                        .sorted(Comparator.comparing(CelebSearchResDto::getCelebTotalNameKr)).toList());

    }

    @Transactional(readOnly = true)
    public List<RecentSelectCelebResDto> getUserRecentSelectCeleb(User user) {
        List<RecentSelectCeleb> recentSelectCelebList = recentSearchCelebRepository.getRecentSelectCelebTop20(user);

        return recentSelectCelebList
                .stream()
                .map(RecentSelectCelebResDto::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CelebSearchResDto> getTop10Celeb() {
        return changeCelebSearchResDto(celebRepository.findTop10Celeb());
    }

    /**
     * Celeb 리스트를 CelebSearchResDto 리스트로 변경
     */
    private List<CelebSearchResDto> changeCelebSearchResDto(List<Celeb> celebList) {
        return celebList.stream()
                .map(CelebSearchResDto::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CelebSearchByCategoryResDto> getCelebByCategory() {
        // Parent Id가 null인 CelebCategory를 모두 조회
        List<CelebCategory> categoryList = celebCategoryRepository.findAllByParentIdIsNull();
        changeCategoryOrder(categoryList);

        return categoryList.stream()
                .parallel()
                // 카테고리별 CelebSearchByCategoryResDto 생성
                .map(category -> CelebSearchByCategoryResDto.of(category,
                                celebRepository.getCelebByCategory(category)
                                        .stream()
                                        // 셀럽별 CelebChipResDto 생성
                                        .map(CelebChipResDto::of)
                                        .toList()
                        )
                ).toList();

    }

    @Transactional(readOnly = true)
    public List<CelebSearchByCategoryResDto> searchInterestedCelebByName(String celebName) {
        // 1. Parent Celeb과 일치
        List<Celeb> celebByParent = celebRepository.searchInterestedCelebByParent(celebName);

        // 2. Child Celeb과 일치
        List<Celeb> celebByChild = celebRepository.searchInterestedCelebByChild(celebName);

        // 1 + 2 를 합친 Celeb
        List<Celeb> celebList = Stream.concat(celebByParent.stream(), celebByChild.stream()).distinct().toList();

        // Celeb의 모든 카테고리 조회
        List<CelebCategory> celebCategoryList = celebCategoryRepository.findAllByParentIdIsNull();

        // Category별 맞는 celeb들을 골라내서 조립.
        return celebCategoryList.stream()
                // 카테고리별 분류
                .map(category -> {
                    List<CelebChipResDto> eachCategoryCeleb =
                            // 카테고리에 맞는 셀럽 filtering
                            celebList.stream().filter(celeb -> {
                                        // ParentCategory가 있다면 ParentCategory. 없다면, CelebCategory.
                                        CelebCategory tempCategory = celeb.getCelebCategory().getParent() != null
                                                ? celeb.getCelebCategory().getParent()
                                                : celeb.getCelebCategory();

                                        return tempCategory == category;
                                        // Category에 맞게 분류된 celeb을 Dto로 변경
                                    }).map(CelebChipResDto::of)
                                    // 가나다 순으로 정렬
                                    .sorted(Comparator.comparing(CelebChipResDto::getCelebName))
                                    .toList();

                    return CelebSearchByCategoryResDto.of(category, eachCategoryCeleb);
                })
                .toList();
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
}
