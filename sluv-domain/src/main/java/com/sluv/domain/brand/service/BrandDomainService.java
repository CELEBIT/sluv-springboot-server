package com.sluv.domain.brand.service;

import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.exception.BrandNotFoundException;
import com.sluv.domain.brand.repository.BrandRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BrandDomainService {
    private final BrandRepository brandRepository;


    @Transactional(readOnly = true)
    public Page<Brand> findByAllBrandKrOrBrandEnStartingWith(String brandName, Pageable pageable) {
        return brandRepository.findByAllBrandKrOrBrandEnStartingWith(brandName, pageable);
    }

    @Transactional(readOnly = true)
    public List<Brand> findTopBrand() {
        return brandRepository.findTop10By();
    }

    @Transactional(readOnly = true)
    public Brand findByIdOrNull(Long brandId) {
        if (brandId == null) {
            return null;
        }
        return brandRepository.findById(brandId).orElseThrow(BrandNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Brand findById(Long brandId) {
        return brandRepository.findById(brandId).orElseThrow(BrandNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Brand> getBrandContainKeyword(String keyword) {
        return brandRepository.getBrandContainKeyword(keyword);
    }
}

