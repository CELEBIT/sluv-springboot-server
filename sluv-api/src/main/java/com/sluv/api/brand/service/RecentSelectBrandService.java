package com.sluv.api.brand.service;

import com.sluv.api.brand.dto.request.RecentSelectBrandRequest;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.brand.entity.RecentSelectBrand;
import com.sluv.domain.brand.service.BrandDomainService;
import com.sluv.domain.brand.service.NewBrandDomainService;
import com.sluv.domain.brand.service.RecentSelectBrandDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecentSelectBrandService {

    private final UserDomainService userDomainService;
    private final BrandDomainService brandDomainService;
    private final NewBrandDomainService newBrandDomainService;
    private final RecentSelectBrandDomainService recentSelectBrandDomainService;

    @Transactional
    public void postRecentSelectBrand(Long userId, RecentSelectBrandRequest request) {
        User user = userDomainService.findById(userId);
        Brand brand = brandDomainService.findByIdOrNull(request.getBrandId());
        NewBrand newBrand = newBrandDomainService.findByIdOrNull(request.getNewBrandId());

        recentSelectBrandDomainService.saveRecentSelectBrand(RecentSelectBrand.toEntity(brand, newBrand, user));
    }

    @Transactional(readOnly = true)
    public void deleteAllRecentSelectBrand(Long userId) {
        recentSelectBrandDomainService.deleteAllByUserId(userId);
    }

    @Transactional
    public void deleteRecentSelectBrand(Long userId, Long brandId, String flag) {
        if (flag.equals("Y")) {
            recentSelectBrandDomainService.deleteByUserIdAndBrandId(userId, brandId);
        } else {
            recentSelectBrandDomainService.deleteByUserIdAndNewBrandId(userId, brandId);
        }
    }

}
