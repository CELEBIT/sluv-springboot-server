package com.sluv.api.domain.brand.service;

import com.sluv.api.brand.dto.response.BrandSearchResponse;
import com.sluv.api.brand.dto.response.RecentSelectBrandResponse;
import com.sluv.api.brand.service.BrandService;
import com.sluv.api.brand.service.RecentSelectBrandService;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.domain.auth.enums.SnsType;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.RecentSelectBrand;
import com.sluv.domain.brand.service.BrandDomainService;
import com.sluv.domain.brand.service.RecentSelectBrandDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserAge;
import com.sluv.domain.user.enums.UserGender;
import com.sluv.domain.user.enums.UserStatus;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class RecentSelectBrandServiceTest {

    @InjectMocks
    private RecentSelectBrandService recentSelectBrandService;


    @Mock
    private RecentSelectBrandDomainService recentSelectBrandDomainService;

    @Mock
    private UserDomainService userDomainService;


    @Test
    @DisplayName("최근 검색한 브랜드를 조회")
    void findRecentSelectBrand() {
        // given
        User user = User.builder()
                .id(1L)
                .build();

        Brand brand = Brand.of("나이키", "NIKE", "http://image.url");
        RecentSelectBrand recentSelectBrand = RecentSelectBrand.builder()
                .brand(brand)
                .user(user)
                .build();

        List<RecentSelectBrand> recentSelectBrands = List.of(recentSelectBrand);

        when(userDomainService.findById(1L)).thenReturn(user);
        when(recentSelectBrandDomainService.getRecentSelectBrandTop20(user)).thenReturn(recentSelectBrands);

        // when
        List<RecentSelectBrandResponse> response = recentSelectBrandService.findRecentSelectBrand(1L);

        // then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getBrandName()).isEqualTo("나이키");
        assertThat(response.get(0).getBrandImgUrl()).isEqualTo("http://image.url");
    }

}
