package com.sluv.api.brand.service;

import com.sluv.api.brand.dto.response.BrandSearchResponse;
import com.sluv.api.brand.dto.response.RecentSelectBrandResponse;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.RecentSelectBrand;
import com.sluv.domain.brand.service.BrandDomainService;
import com.sluv.domain.brand.service.RecentSelectBrandDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final UserDomainService userDomainService;
    private final BrandDomainService brandDomainService;
    private final RecentSelectBrandDomainService recentSelectBrandDomainService;

    public PaginationResponse<BrandSearchResponse> findAllBrand(String brandName, Pageable pageable) {
        Page<Brand> brandPage = brandDomainService.findByAllBrandKrOrBrandEnStartingWith(brandName, pageable);

        List<BrandSearchResponse> dtoList = brandPage.stream()
                .map(BrandSearchResponse::of)
                .toList();

        return PaginationResponse.create(brandPage, dtoList);
    }

    public List<BrandSearchResponse> findTopBrand() {
        return brandDomainService.findTopBrand()
                .stream()
                .map(BrandSearchResponse::of)
                .toList();
    }

    public List<RecentSelectBrandResponse> findRecentSelectBrand(Long userId) {
        User user = userDomainService.findById(userId);
        List<RecentSelectBrand> recentSelectBrandList = recentSelectBrandDomainService.getRecentSelectBrandTop20(user);

        return recentSelectBrandList.stream()
                .map(RecentSelectBrandResponse::of)
                .toList();
    }

}

