package com.sluv.api.celeb.service;

import com.sluv.api.celeb.dto.request.NewCelebPostRequest;
import com.sluv.api.celeb.dto.response.NewCelebPostResponse;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.service.NewCelebDomainService;
import com.sluv.infra.discord.WebHookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewCelebService {

    private final NewCelebDomainService newCelebDomainService;
    private final WebHookService webHookService;

    @Transactional
    public NewCelebPostResponse postNewCeleb(NewCelebPostRequest dto) {
        NewCeleb newCeleb = newCelebDomainService.findByCelebNameOrNull(dto.getNewCelebName());

        if (newCeleb == null) {
            newCeleb = newCelebDomainService.saveNewCelebByName(NewCeleb.toEntity(dto.getNewCelebName()));
            webHookService.sendCreateNewCelebMessage(newCeleb);
        }

        return NewCelebPostResponse.of(newCeleb);
    }

}
