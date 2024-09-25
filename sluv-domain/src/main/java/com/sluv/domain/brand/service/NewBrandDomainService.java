package com.sluv.domain.brand.service;

import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.brand.exception.NewBrandNotFoundException;
import com.sluv.domain.brand.repository.NewBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewBrandDomainService {

    private final NewBrandRepository newBrandRepository;

    @Transactional
    public NewBrand findByBrandName(String newBrandName) {
        return newBrandRepository.findByBrandName(newBrandName).orElse(null);
    }

    @Transactional
    public NewBrand saveNewBrand(NewBrand newBrand) {
        return newBrandRepository.save(newBrand);
    }

    @Transactional(readOnly = true)
    public NewBrand findByIdOrNull(Long newBrandId) {
        if (newBrandId == null) {
            return null;
        }
        return newBrandRepository.findById(newBrandId).orElseThrow(NewBrandNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public NewBrand findById(Long newBrandId) {
        return newBrandRepository.findById(newBrandId).orElseThrow(NewBrandNotFoundException::new);
    }

}
