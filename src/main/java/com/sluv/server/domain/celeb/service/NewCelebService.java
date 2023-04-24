package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.celeb.dto.NewCelebPostReqDto;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.enums.NewCelebStatus;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewCelebService {

    private final NewCelebRepository newCelebRepository;

    public Long postNewCeleb(NewCelebPostReqDto dto){
        return newCelebRepository.save(NewCeleb.builder()
                .celebName(dto.getNewCelebName())
                .newCelebStatus(NewCelebStatus.ACTIVE)
                .build()
        ).getId();
    }
}
