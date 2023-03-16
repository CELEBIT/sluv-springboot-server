package com.sluv.server.domain.brand.service;

import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public List<Brand> findAllBrand(String brandName){

        return brandRepository.findAllByBrandKrStartingWith(brandName);
    }
}
