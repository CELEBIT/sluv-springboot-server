package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.celeb.dto.NewCelebPostReqDto;
import com.sluv.server.domain.celeb.dto.NewCelebPostResDto;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewCelebService {

    private final NewCelebRepository newCelebRepository;

    @Transactional
    public NewCelebPostResDto postNewCeleb(NewCelebPostReqDto dto) {
        NewCeleb newCeleb = newCelebRepository.save(
                NewCeleb.toEntity(dto)
        );

        return NewCelebPostResDto.of(newCeleb);
    }
}
