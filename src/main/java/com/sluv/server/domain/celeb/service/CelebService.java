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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        // Parent Id가 null인 CelebCategory를 모두 조회 후 이름순으로 정렬
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
}
