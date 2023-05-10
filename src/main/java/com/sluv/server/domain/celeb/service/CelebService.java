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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    public List<CelebSearchResDto> searchCeleb(String celebName, Pageable pageable) {

        return celebRepository.searchCeleb(celebName, pageable).stream()
                .collect(Collectors.partitioningBy(c -> c.getParent() == null))
                .entrySet().stream()
                .flatMap(entry -> {
                    if (entry.getKey()) {
                        // 검색어가 parent Celeb 일 때
                        return entry.getValue().stream()
                                .collect(Collectors.partitioningBy(c -> c.getSubCelebList() == null || c.getSubCelebList().size() == 0))
                                .entrySet().stream()
                                .flatMap(parentEntry -> {
                                    // Celeb이 솔로일 경우
                                    if(parentEntry.getKey()){
                                        return parentEntry.getValue().stream()
                                                        .map(data -> {
                                                            String category;
                                                            if(data.getCelebCategory().getParent() != null){
                                                                category = data.getCelebCategory().getParent().getName();
                                                            }else {
                                                                category = data.getCelebCategory().getName();
                                                            }

                                                            return CelebSearchResDto.builder()
                                                                            .id(data.getId())
                                                                            .parentId(null)
                                                                            .category(category)
                                                                            .celebNameKr(data.getCelebNameKr())
                                                                            .celebNameEn(data.getCelebNameEn())
                                                                            .build();
                                                                }

                                                        );

                                    }else{
                                        // Celeb이 그룹명일 경우
                                        return parentEntry.getValue().stream()
                                                .flatMap(parent -> parent.getSubCelebList().stream()
                                                        .map(child -> {
                                                            String category;
                                                            if(child.getCelebCategory().getParent() != null){
                                                                category = child.getCelebCategory().getParent().getName();
                                                            }else {
                                                                category = child.getCelebCategory().getName();
                                                            }
                                                            return CelebSearchResDto.builder()
                                                                            .id(child.getId())
                                                                            .parentId(parent.getId())
                                                                            .category(category)
                                                                            .celebNameKr(parent.getCelebNameKr() + " " + child.getCelebNameKr())
                                                                            .celebNameEn(parent.getCelebNameEn() + " " + child.getCelebNameEn())
                                                                            .build();
                                                                }
                                                        )
                                                );
                                    }
                                });

                    } else {
                        // 검색어가 child Celeb 일 때
                        return entry.getValue().stream()
                                .map(child -> {
                                        String category;
                                        if(child.getCelebCategory().getParent() != null){
                                            category = child.getCelebCategory().getParent().getName();
                                        }else{
                                            category = child.getCelebCategory().getName();
                                        }
                                        return CelebSearchResDto.builder()
                                                .id(child.getId())
                                                .parentId(child.getParent().getId())
                                                .category(category)
                                                .celebNameKr(child.getParent().getCelebNameKr() + " " + child.getCelebNameKr())
                                                .celebNameEn(child.getParent().getCelebNameEn() + " " + child.getCelebNameEn())
                                                .build();
                                    }
                                );
                    }

                }).toList();

    }

    public List<RecentSelectCelebResDto> getUserRecentSelectCeleb(User user){
        List<RecentSelectCeleb> recentSelectCelebList = recentSearchCelebRepository.getRecentSelectCelebTop20(user);

        return recentSelectCelebList.stream().map(recentSelectCeleb -> {
            Long celebId;
            String celebName;
            String flag = recentSelectCeleb.getCeleb() != null ? "Y" :"N";
            if(flag.equals("Y")){
                celebId = recentSelectCeleb.getCeleb().getId();
                celebName = recentSelectCeleb.getCeleb().getCelebNameKr();
            }else{
                celebId = recentSelectCeleb.getNewCeleb().getId();
                celebName = recentSelectCeleb.getNewCeleb().getCelebName();
            }
            return RecentSelectCelebResDto.builder()
                    .id(celebId)
                    .celebName(celebName)
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
                .map(celeb -> {
                            String celebNameKr = celeb.getCelebNameKr();
                            String celebNameEn = celeb.getCelebNameEn();

                            if (celeb.getParent() != null){
                                celebNameKr = celeb.getParent().getCelebNameKr() + " " + celeb.getCelebNameKr();
                                celebNameEn = celeb.getParent().getCelebNameEn() + " " + celeb.getCelebNameEn();
                            }


                            return CelebSearchResDto.builder()
                                    .id(celeb.getId())
                                    .parentId(celeb.getParent().getId())
                                    .category(celeb.getCelebCategory().getParent().getName())
                                    .celebNameKr(celebNameKr)
                                    .celebNameEn(celebNameEn)
                                    .build();
                        }

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
