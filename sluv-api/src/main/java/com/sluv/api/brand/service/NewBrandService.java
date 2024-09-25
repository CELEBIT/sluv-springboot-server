package com.sluv.api.brand.service;

import com.sluv.api.brand.dto.request.NewBrandPostRequest;
import com.sluv.api.brand.dto.response.NewBrandPostResponse;
import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.brand.service.NewBrandDomainService;
import com.sluv.infra.discord.WebHookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewBrandService {

    private final NewBrandDomainService newBrandDomainService;
    private final WebHookService webHookService;

    public NewBrandPostResponse postNewBrand(NewBrandPostRequest request) {
        NewBrand newBrand = newBrandDomainService.findByBrandName(request.getNewBrandName());

        if (newBrand == null) {
            newBrand = newBrandDomainService.saveNewBrand(NewBrand.toEntity(request.getNewBrandName()));
            webHookService.sendCreateNewBrandMessage(newBrand);
        }

        return NewBrandPostResponse.of(newBrand);
    }


}
