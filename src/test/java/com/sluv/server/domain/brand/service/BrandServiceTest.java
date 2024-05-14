package com.sluv.server.domain.brand.service;

import static com.sluv.server.fixture.UserFixture.카카오_유저_생성;
import static org.assertj.core.api.Assertions.assertThat;

import com.sluv.server.domain.brand.dto.BrandSearchResDto;
import com.sluv.server.domain.brand.dto.RecentSelectBrandResDto;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.RecentSelectBrand;
import com.sluv.server.domain.brand.repository.BrandRepository;
import com.sluv.server.domain.brand.repository.NewBrandRepository;
import com.sluv.server.domain.brand.repository.RecentSelectBrandRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.response.PaginationResDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class BrandServiceTest {

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private NewBrandRepository newBrandRepository;

    @Autowired
    private RecentSelectBrandRepository recentSelectBrandRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clear() {
        recentSelectBrandRepository.deleteAll();
        brandRepository.deleteAll();
        newBrandRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("이름으로 브랜드를 검색한다.")
    @Test
    void findAllBrandByNameTest() {
        //given
        Brand brand1 = Brand.builder().brandKr("브랜드1").brandEn("brand1").build();
        Brand brand2 = Brand.builder().brandKr("브랜드2").brandEn("brand2").build();
        Brand brand3 = Brand.builder().brandKr("브랜드3").brandEn("brand3").build();

        brandRepository.saveAll(List.of(brand1, brand2, brand3));

        //when
        PaginationResDto<BrandSearchResDto> brand = brandService.findAllBrand("브랜드", PageRequest.of(0, 2));

        //then
        assertThat(brand.getContent().get(0)).extracting("brandKr").isEqualTo("브랜드1");
        assertThat(brand.getContent().get(1)).extracting("brandKr").isEqualTo("브랜드2");
        assertThat(brand).extracting("hasNext").isEqualTo(true);
    }

    @DisplayName("상위 10의 브랜드를 조회한다.")
    @Test
    void findTopBrandTest() {
        //given
        User user = userRepository.save(카카오_유저_생성());

        List<Brand> brands = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            Brand brand = Brand.builder().brandKr("브랜드" + i).brandEn("brand" + i).build();
            brands.add(brand);
        }
        brandRepository.saveAll(brands);

        List<RecentSelectBrand> recentSelectBrands = new ArrayList<>();
        RecentSelectBrand recentSelectBrand1 = RecentSelectBrand.toEntity(brands.get(0), null, user);
        RecentSelectBrand recentSelectBrand2 = RecentSelectBrand.toEntity(brands.get(0), null, user);
        RecentSelectBrand recentSelectBrand3 = RecentSelectBrand.toEntity(brands.get(0), null, user);
        RecentSelectBrand recentSelectBrand4 = RecentSelectBrand.toEntity(brands.get(1), null, user);
        RecentSelectBrand recentSelectBrand5 = RecentSelectBrand.toEntity(brands.get(1), null, user);

        recentSelectBrands.addAll(List.of(recentSelectBrand1, recentSelectBrand2, recentSelectBrand3,
                recentSelectBrand4, recentSelectBrand5));

        for (int i = 2; i <= 10; i++) {
            RecentSelectBrand recentSelectBrand = RecentSelectBrand.toEntity(brands.get(i), null, user);
            recentSelectBrands.add(recentSelectBrand);
        }
        recentSelectBrandRepository.saveAll(recentSelectBrands);

        //when
        List<BrandSearchResDto> topBrand = brandService.findTopBrand();

        //then
        assertThat(topBrand.get(0)).extracting("brandKr").isEqualTo("브랜드1");
        assertThat(topBrand.get(1)).extracting("brandKr").isEqualTo("브랜드2");
        assertThat(topBrand).hasSize(10);
    }

    @DisplayName("최근 선택한 브랜드를 조회할 수 있다.")
    @Test
    void findRecentSelectBrandTest() {
        //given
        User user = userRepository.save(카카오_유저_생성());
        Brand brand1 = Brand.builder().brandKr("브랜드1").brandEn("brand1").build();
        Brand brand2 = Brand.builder().brandKr("브랜드2").brandEn("brand2").build();
        Brand brand3 = Brand.builder().brandKr("브랜드3").brandEn("brand3").build();
        List<Brand> brands = brandRepository.saveAll(List.of(brand1, brand2, brand3));

        RecentSelectBrand recentSelectBrand1 = RecentSelectBrand.toEntity(brands.get(0), null, user);
        RecentSelectBrand recentSelectBrand2 = RecentSelectBrand.toEntity(brands.get(1), null, user);
        RecentSelectBrand recentSelectBrand3 = RecentSelectBrand.toEntity(brands.get(2), null, user);
        RecentSelectBrand recentSelectBrand4 = RecentSelectBrand.toEntity(brands.get(0), null, user);
        List<RecentSelectBrand> recentSelectBrands = List.of(recentSelectBrand1, recentSelectBrand2,
                recentSelectBrand3, recentSelectBrand4);

        recentSelectBrandRepository.saveAll(recentSelectBrands);

        //when
        List<RecentSelectBrandResDto> recentSelectBrand = brandService.findRecentSelectBrand(user);

        //then
        assertThat(recentSelectBrand.get(0)).extracting("brandName").isEqualTo("브랜드1");
        assertThat(recentSelectBrand.get(1)).extracting("brandName").isEqualTo("브랜드3");
    }
}
