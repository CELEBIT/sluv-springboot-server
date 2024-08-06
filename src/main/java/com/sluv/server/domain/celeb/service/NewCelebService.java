package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.celeb.dto.NewCelebPostReqDto;
import com.sluv.server.domain.celeb.dto.NewCelebPostResDto;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import com.sluv.server.global.discord.WebHookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewCelebService {

    private final NewCelebRepository newCelebRepository;
    private final WebHookService webHookService;

    @Transactional
    public NewCelebPostResDto postNewCeleb(NewCelebPostReqDto dto) {
        NewCeleb newCeleb = newCelebRepository.findByCelebName(dto.getNewCelebName()).orElse(null);

        if (newCeleb == null) {
            newCeleb = newCelebRepository.save(NewCeleb.toEntity(dto));
            webHookService.sendCreateNewCelebMessage(newCeleb);
        }

        return NewCelebPostResDto.of(newCeleb);
    }
}
