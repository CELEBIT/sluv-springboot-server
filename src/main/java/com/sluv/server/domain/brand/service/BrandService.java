package com.sluv.server.domain.brand.service;

import com.sluv.server.domain.brand.dto.BrandSearchResDto;
import com.sluv.server.domain.brand.dto.RecentSelectBrandResDto;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.RecentSelectBrand;
import com.sluv.server.domain.brand.repository.BrandRepository;
import com.sluv.server.domain.brand.repository.RecentSelectBrandRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final RecentSelectBrandRepository recentSelectBrandRepository;
    private final UserRepository userRepository;


    public PaginationResDto<BrandSearchResDto> findAllBrand(String brandName, Pageable pageable){

        Page<Brand> brandPage = brandRepository.findByAllBrandKrOrBrandEnStartingWith(brandName, pageable);

        List<BrandSearchResDto> dtoList = brandPage.stream()
                .map(BrandSearchResDto::of)
                .toList();

        return PaginationResDto.<BrandSearchResDto>builder()
                .hasNext(brandPage.hasNext())
                .page(brandPage.getNumber())
                .content(dtoList)
                .build();


    }

    public List<BrandSearchResDto> findTopBrand() {
        return brandRepository.findTop10By().stream()
                                            .map(BrandSearchResDto::of
                                            ).toList();
    }

    public List<RecentSelectBrandResDto> findRecentSelectBrand(User user) {

        List<RecentSelectBrand> recentSelectBrandList = recentSelectBrandRepository.getRecentSelectBrandTop20(user);

        return recentSelectBrandList.stream()
                .map(RecentSelectBrandResDto::of)
                .toList();
    }
}

