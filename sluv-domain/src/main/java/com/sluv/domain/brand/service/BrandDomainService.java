package com.sluv.domain.brand.service;

import com.sluv.domain.brand.dto.BrandCountDto;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.exception.BrandNotFoundException;
import com.sluv.domain.brand.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandDomainService {
    private final BrandRepository brandRepository;


    public Page<Brand> findByAllBrandKrOrBrandEnStartingWith(String brandName, Pageable pageable) {
        return brandRepository.findByAllBrandKrOrBrandEnStartingWith(brandName, pageable);
    }

    public List<Brand> findTopBrand() {
        return brandRepository.findTop10By();
    }

    public Brand findByIdOrNull(Long brandId) {
        if (brandId == null) {
            return null;
        }
        return brandRepository.findById(brandId).orElseThrow(BrandNotFoundException::new);
    }

    public Brand findById(Long brandId) {
        return brandRepository.findById(brandId).orElseThrow(BrandNotFoundException::new);
    }

    public List<Brand> getBrandContainKeyword(String keyword) {
        return brandRepository.getBrandContainKeyword(keyword);
    }

    public List<BrandCountDto> getTopHotBrandWithLimit(int limitCount) {
        return brandRepository.getTopHotBrandWithLimit(limitCount);
    }

    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public Page<Brand> findAllWithPageable(Pageable pageable) {
        return brandRepository.findAllWithPageable(pageable);
    }


}

