package com.sluv.admin.brand.service;

import com.sluv.admin.brand.dto.NewBrandChangeRequest;
import com.sluv.admin.brand.dto.NewBrandRegisterDto;
import com.sluv.admin.brand.dto.NewBrandRegisterRequest;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.brand.service.BrandDomainService;
import com.sluv.domain.brand.service.NewBrandDomainService;
import com.sluv.domain.brand.service.RecentSelectBrandDomainService;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.TempItemDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewBrandService {

    private final NewBrandDomainService newBrandDomainService;
    private final BrandDomainService brandDomainService;
    private final ItemDomainService itemDomainService;
    private final TempItemDomainService tempItemDomainService;
    private final RecentSelectBrandDomainService recentSelectBrandDomainService;

    @Transactional(readOnly = true)
    public List<NewBrandRegisterDto> findAllNewBrandRegisterDto() {
        return newBrandDomainService.findAll().stream()
                .map(NewBrandRegisterDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public NewBrandRegisterDto findByNewBrandId(Long brandId) {
        NewBrand newBrand = newBrandDomainService.findById(brandId);
        return NewBrandRegisterDto.from(newBrand);
    }

    @Transactional
    public void changeNewBrandToBrand(NewBrandChangeRequest request) {
        Brand brand = brandDomainService.findById(request.getBrandId());

        // 뉴브랜드를 브랜드로 변경
        itemDomainService.changeAllBrandByNewBrandId(brand, request.getNewBrandId());
        tempItemDomainService.changeAllBrandByNewBrandId(brand, request.getNewBrandId());
        recentSelectBrandDomainService.changeAllBrandByNewBrandId(brand, request.getNewBrandId());
        newBrandDomainService.deleteById(request.getNewBrandId());
    }

    @Transactional
    public void registerNewBrandToBrand(NewBrandRegisterRequest request) {
        // 브랜드 생성
        Brand brand = brandDomainService.saveBrand(Brand.of(request.getKrName(),
                request.getEnName(),
                request.getImageUrl())
        );

        // 뉴브랜드를 브랜드로 변경
        itemDomainService.changeAllBrandByNewBrandId(brand, request.getNewBrandId());
        tempItemDomainService.changeAllBrandByNewBrandId(brand, request.getNewBrandId());
        recentSelectBrandDomainService.changeAllBrandByNewBrandId(brand, request.getNewBrandId());
        newBrandDomainService.deleteById(request.getNewBrandId());
    }
}
