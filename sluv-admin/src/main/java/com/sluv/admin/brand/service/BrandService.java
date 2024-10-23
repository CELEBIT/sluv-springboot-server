package com.sluv.admin.brand.service;

import com.sluv.admin.brand.dto.BrandPageResponse;
import com.sluv.admin.brand.dto.BrandResponse;
import com.sluv.admin.brand.dto.HotBrandResDto;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.service.BrandDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandDomainService brandDomainService;

    public List<HotBrandResDto> getTop3HotBrand() {
        return brandDomainService.getTopHotBrandWithLimit(3).stream()
                .map(brandCountDto -> HotBrandResDto.of(brandCountDto.getBrand(), brandCountDto.getUseCount()))
                .toList();
    }

    public BrandPageResponse findAllNewBrandRegisterDto(String keyword, Integer page) {
        Pageable pageable = getPageable(page);
        Page<Brand> brandPage = getBrandPage(keyword, pageable);
        List<BrandResponse> content = brandPage.stream().map(BrandResponse::from).toList();

        return BrandPageResponse.of(brandPage.getNumber(), brandPage.getTotalPages(), content);
    }

    private Page<Brand> getBrandPage(String keyword, Pageable pageable) {
        Page<Brand> brandPage;
        if (keyword != null) {
            brandPage = brandDomainService.findByAllBrandKrOrBrandEnStartingWith(keyword, pageable);
        } else {
            brandPage = brandDomainService.findAllWithPageable(pageable);
        }
        return brandPage;
    }

    private Pageable getPageable(Integer page) {
        if (page == null) {
            page = 1;
        }
        return PageRequest.of(page - 1, 20);
    }
}
