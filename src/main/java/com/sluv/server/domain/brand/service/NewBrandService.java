package com.sluv.server.domain.brand.service;

import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.brand.enums.NewBrandStatus;
import com.sluv.server.domain.brand.repository.NewBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewBrandService {
    private NewBrandRepository newBrandRepository;

    public Long postNewBrand(String newBrandName){
        return newBrandRepository.save(
                NewBrand.builder()
                    .brandName(newBrandName)
                    .newBrandStatus(NewBrandStatus.ACTIVE)
                    .build()
                ).getId();
    }

}
