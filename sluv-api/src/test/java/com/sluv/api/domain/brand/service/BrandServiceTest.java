package com.sluv.api.domain.brand.service;

import com.sluv.api.brand.dto.response.BrandSearchResponse;
import com.sluv.api.brand.service.BrandService;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.service.BrandDomainService;
import com.sluv.domain.brand.service.RecentSelectBrandDomainService;
import com.sluv.domain.user.service.UserDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class BrandServiceTest {

    @InjectMocks
    private BrandService brandService;


    @Mock
    private BrandDomainService brandDomainService;


    @Test
    @DisplayName("한글 이름으로 브랜드 검색")
    void findAllBrandWithKrNameTest() {
        // given
        String keyword = "나";
        PageRequest pageable = PageRequest.of(0, 1);
        Brand brand = Brand.of("나이키", "NIKE", "http://image.url");
        List<Brand> content = List.of(brand);
        Page<Brand> brandPage = new PageImpl<>(content, pageable, content.size());
        when(brandDomainService.findByAllBrandKrOrBrandEnStartingWith(keyword, pageable)).thenReturn(brandPage);

        // when
        PaginationResponse<BrandSearchResponse> response = brandService.findAllBrand(keyword, pageable);

        // then
        assertThat(response.getContent().size()).isEqualTo(1);
        assertThat(response.getContent().get(0).getBrandKr()).isEqualTo("나이키");
    }

    @Test
    @DisplayName("영어 이름으로 브랜드 검색")
    void findAllBrandWithEnNameTest() {
        // given
        String keyword = "N";
        PageRequest pageable = PageRequest.of(0, 1);
        Brand brand = Brand.of("나이키", "NIKE", "http://image.url");
        List<Brand> content = List.of(brand);
        Page<Brand> brandPage = new PageImpl<>(content, pageable, content.size());
        when(brandDomainService.findByAllBrandKrOrBrandEnStartingWith(keyword, pageable)).thenReturn(brandPage);

        // when
        PaginationResponse<BrandSearchResponse> response = brandService.findAllBrand(keyword, pageable);

        // then
        assertThat(response.getContent().size()).isEqualTo(1);
        assertThat(response.getContent().get(0).getBrandEn()).isEqualTo("NIKE");
    }



}
