package com.sluv.admin.brand.service;

import com.sluv.admin.brand.dto.HotBrandResDto;
import com.sluv.domain.brand.service.BrandDomainService;
import lombok.RequiredArgsConstructor;
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
}
