package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.enums.NewCelebStatus;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewCelebService {

    private NewCelebRepository newCelebRepository;

    public Long postNewCeleb(String newCelebName){
        return newCelebRepository.save(NewCeleb.builder()
                .celebName(newCelebName)
                .newCelebStatus(NewCelebStatus.ACTIVE)
                .build()
        ).getId();
    }
}
