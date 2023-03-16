package com.sluv.server.domain.brand.service;

import com.sluv.server.domain.brand.dto.BrandSearchResDto;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public List<BrandSearchResDto> findAllBrand(String brandName){

        return brandRepository.findAllByBrandKrStartingWith(brandName).stream()
                                                                    .map(data -> BrandSearchResDto.builder()
                                                                                                    .id(data.getId())
                                                                                                    .brandKr(data.getBrandKr())
                                                                                                    .brandEn(data.getBrandEn())
                                                                                                    .brandImgUrl(data.getBrandImgUrl())
                                                                                                    .build()
                                                                    ).collect(Collectors.toList());
    }
}
