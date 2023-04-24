package com.sluv.server.domain.brand.service;

import com.sluv.server.domain.brand.dto.RecentSelectBrandReqDto;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.brand.entity.RecentSelectBrand;
import com.sluv.server.domain.brand.exception.BrandNotFoundException;
import com.sluv.server.domain.brand.exception.NewBrandNotFoundException;
import com.sluv.server.domain.brand.repository.BrandRepository;
import com.sluv.server.domain.brand.repository.NewBrandRepository;
import com.sluv.server.domain.brand.repository.RecentSelectBrandRepository;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecentSelectBrandService {
    private final BrandRepository brandRepository;
    private final NewBrandRepository newBrandRepository;
    private final RecentSelectBrandRepository recentSelectBrandRepository;

    public void postRecentSelectBrand(User user, RecentSelectBrandReqDto dto){
        Brand brand = dto.getBrandId() != null
                ? brandRepository.findById(dto.getBrandId())
                                .orElseThrow(BrandNotFoundException::new)
                : null;

        NewBrand newBrand = dto.getNewBrandId() != null
                ? newBrandRepository.findById(dto.getNewBrandId())
                .orElseThrow(NewBrandNotFoundException::new)
                : null;

        recentSelectBrandRepository.save(
                RecentSelectBrand.builder()
                        .brand(brand)
                        .newBrand(newBrand)
                        .user(user)
                        .build()
        );
    }

    @Transactional
    public void deleteAllRecentSelectBrand(User user) {
        recentSelectBrandRepository.deleteAllByUserId(user.getId());
    }
}
