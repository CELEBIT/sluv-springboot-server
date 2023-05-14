package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.celeb.dto.*;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.CelebCategory;
import com.sluv.server.domain.celeb.entity.InterestedCeleb;
import com.sluv.server.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.server.domain.celeb.repository.CelebCategoryRepository;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.RecentSelectCelebRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CelebService {

    private final CelebRepository celebRepository;
    private final RecentSelectCelebRepository recentSearchCelebRepository;
    private final CelebCategoryRepository celebCategoryRepository;

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


        return PaginationResDto.<CelebSearchResDto>builder()
                .page(celebPage.getNumber())
                .hasNext(celebPage.hasNext())
                .content(Stream.concat(childCelebDtoStream, parentCelebDtoStream).sorted(Comparator.comparing(CelebSearchResDto::getCelebTotalNameKr)).toList())
                .build();

    }

    public List<RecentSelectCelebResDto> getUserRecentSelectCeleb(User user){
        List<RecentSelectCeleb> recentSelectCelebList = recentSearchCelebRepository.getRecentSelectCelebTop20(user);

        return recentSelectCelebList.stream().map(recentSelectCeleb -> {
            Long celebChildId;
            Long celebParentId;
            String celebChildName;
            String celebParentName;

            String flag = recentSelectCeleb.getCeleb() != null ? "Y" :"N";
            if(flag.equals("Y")){
                celebChildId = recentSelectCeleb.getCeleb().getId();
                celebParentId = recentSelectCeleb.getCeleb().getParent() != null
                        ? recentSelectCeleb.getCeleb().getParent().getId()
                        : null;
                celebChildName = recentSelectCeleb.getCeleb().getCelebNameKr();
                celebParentName = recentSelectCeleb.getCeleb().getParent() != null
                        ? recentSelectCeleb.getCeleb().getParent().getCelebNameKr()
                        : null;
            }else{
                celebChildId = recentSelectCeleb.getNewCeleb().getId();
                celebParentId = null;
                celebChildName = recentSelectCeleb.getNewCeleb().getCelebName();
                celebParentName = null;
            }
            return RecentSelectCelebResDto.builder()
                    .id(celebChildId)
                    .parentId(celebParentId)
                    .childCelebName(celebChildName)
                    .parentCelebName(celebParentName)
                    .flag(flag)
                    .build();
        }).toList();


    }

    public List<CelebSearchResDto> getTop10Celeb(){
        return  changeCelebSearchResDto(celebRepository.findTop10Celeb());
    }

    /**
     * Celeb 리스트를 CelebSearchResDto 리스트로 변경
     */
    private List<CelebSearchResDto> changeCelebSearchResDto(List<Celeb> celebList){
        return celebList.stream()
                .map(celeb ->
                         CelebSearchResDto.builder()
                                .id(celeb.getId())
                                .parentId(
                                        celeb.getParent() != null
                                        ? celeb.getParent().getId()
                                        : null
                                )
                                .category(celeb.getCelebCategory().getParent() != null
                                        ? celeb.getCelebCategory().getParent().getName()
                                        : celeb.getCelebCategory().getName()
                                        )
                                .celebParentNameKr(
                                        celeb.getParent() != null
                                        ? celeb.getParent().getCelebNameKr()
                                        :null
                                        )
                                .celebChildNameKr(celeb.getCelebNameKr())
                                .celebTotalNameKr(
                                        celeb.getParent() != null
                                        ? celeb.getParent().getCelebNameKr() + " " + celeb.getCelebNameKr()
                                        : celeb.getCelebNameKr()
                                        )
                                .celebTotalNameEn(
                                        celeb.getParent() != null
                                                ? celeb.getParent().getCelebNameEn() + " " + celeb.getCelebNameEn()
                                                : celeb.getCelebNameEn()
                                )
                                .build()

                ).toList();
    }

    public List<CelebSearchByCategoryResDto> getCelebByCategory() {
        // Parent Id가 null인 CelebCategory를 모두 조회
        List<CelebCategory> categoryList = celebCategoryRepository.findAllByParentIdIsNull();

        return categoryList.stream()
                            .parallel()
                            // 카테고리별 CelebSearchByCategoryResDto 생성
                            .map(category ->  CelebSearchByCategoryResDto.builder()
                                                                        .categoryId(category.getId())
                                                                        .categoryName(category.getName())
                                                                        .celebList(
                                                                                celebRepository.getCelebByCategory(category)
                                                                                        .stream()
                                                                                        // 셀럽별 CelebChipResDto 생성
                                                                                        .map(celeb -> CelebChipResDto.builder()
                                                                                                .celebId(celeb.getId())
                                                                                                .celebName(celeb.getCelebNameKr())
                                                                                                .build()
                                                                                        ).toList()
                                                                        ).build()
                            ).toList();

    }

    public List<InterestedCelebParentResDto> searchInterestedCelebByName(String celebName) {
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
                .map(category ->{
                             List<InterestedCelebChildResDto> eachCategoryCeleb =
                                     // 카테고리에 맞는 셀럽 filtering
                                     celebList.stream().filter(celeb -> {
                                        // ParentCategory가 있다면 ParentCategory. 없다면, CelebCategory.
                                        CelebCategory tempCategory = celeb.getCelebCategory().getParent() != null
                                                ? celeb.getCelebCategory().getParent()
                                                : celeb.getCelebCategory();

                                        return tempCategory == category;
                                        // Category에 맞게 분류된 celeb을 Dto로 변경
                                    }).map(celeb -> InterestedCelebChildResDto.builder()
                                                         .id(celeb.getId())
                                                         .celebNameKr(celeb.getCelebNameKr())
                                                         .build()
                            )
                             // 가나다 순으로 정렬
                            .sorted(Comparator.comparing(InterestedCelebChildResDto::getCelebNameKr))
                            .toList();

                 return InterestedCelebParentResDto.builder()
                         .id(category.getId())
                         .celebNameKr(category.getName())
                         .subCelebList(eachCategoryCeleb)
                         .build();
                })
                .toList();
    }
}
