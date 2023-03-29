package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.celeb.dto.CelebSearchResDto;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CelebService {

    private final CelebRepository celebRepository;

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
                                                        .map(data -> CelebSearchResDto.builder()
                                                                .id(data.getId())
                                                                .celebNameKr(data.getCelebNameKr())
                                                                .celebNameEn(data.getCelebNameEn())
                                                                .build()
                                                        );

                                    }else{
                                        // Celeb이 그룹명일 경우
                                        return parentEntry.getValue().stream()
                                                .flatMap(parent -> parent.getSubCelebList().stream()
                                                        .map(child -> CelebSearchResDto.builder()
                                                                .id(child.getId())
                                                                .celebNameKr(parent.getCelebNameKr() + " " + child.getCelebNameKr())
                                                                .celebNameEn(parent.getCelebNameEn() + " " + child.getCelebNameEn())
                                                                .build()
                                                        )
                                                );
                                    }
                                });

                    } else {
                        // 검색어가 child Celeb 일 때
                        return entry.getValue().stream()
                                .map(child ->
                                        CelebSearchResDto.builder()
                                                .id(child.getId())
                                                .celebNameKr(child.getParent().getCelebNameKr() + " " + child.getCelebNameKr())
                                                .celebNameEn(child.getParent().getCelebNameEn() + " " + child.getCelebNameEn())
                                                .build()
                                );
                    }

                }).toList();

    }
}
