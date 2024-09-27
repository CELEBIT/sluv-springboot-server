package com.sluv.domain.brand.service;

import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.brand.exception.NewBrandNotFoundException;
import com.sluv.domain.brand.repository.NewBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewBrandDomainService {

    private final NewBrandRepository newBrandRepository;

    public NewBrand findByBrandName(String newBrandName) {
        return newBrandRepository.findByBrandName(newBrandName).orElse(null);
    }

    public NewBrand saveNewBrand(NewBrand newBrand) {
        return newBrandRepository.save(newBrand);
    }

    public NewBrand findByIdOrNull(Long newBrandId) {
        if (newBrandId == null) {
            return null;
        }
        return newBrandRepository.findById(newBrandId).orElseThrow(NewBrandNotFoundException::new);
    }

    public NewBrand findById(Long newBrandId) {
        return newBrandRepository.findById(newBrandId).orElseThrow(NewBrandNotFoundException::new);
    }

}
