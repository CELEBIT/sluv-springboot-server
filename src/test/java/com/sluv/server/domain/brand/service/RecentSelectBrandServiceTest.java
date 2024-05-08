package com.sluv.server.domain.brand.service;

import static com.sluv.server.fixture.UserFixture.카카오_유저_생성;
import static org.assertj.core.api.Assertions.assertThat;

import com.sluv.server.domain.brand.dto.RecentSelectBrandReqDto;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.RecentSelectBrand;
import com.sluv.server.domain.brand.repository.BrandRepository;
import com.sluv.server.domain.brand.repository.NewBrandRepository;
import com.sluv.server.domain.brand.repository.RecentSelectBrandRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RecentSelectBrandServiceTest {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private NewBrandRepository newBrandRepository;

    @Autowired
    private RecentSelectBrandService recentSelectBrandService;

    @Autowired
    private RecentSelectBrandRepository recentSelectBrandRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clear() {
        recentSelectBrandRepository.deleteAll();
        newBrandRepository.deleteAll();
        brandRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("최근 검색한 브랜드를 저장한다.")
    @Test
    void postRecentSelectBrandTest() {
        //given
        User user = 카카오_유저_생성();
        userRepository.save(user);
        Brand brand = Brand.builder()
                .brandKr("브랜드1")
                .brandEn("brand1")
                .brandImgUrl(null)
                .build();
        brandRepository.save(brand);

        //when
        recentSelectBrandService.postRecentSelectBrand(user, new RecentSelectBrandReqDto(brand.getId(), null));
        //then
        assertThat(recentSelectBrandRepository.findAll()).hasSize(1);
    }

    @DisplayName("최근 검색한 브랜드를 모두 삭제한다.")
    @Test
    void deleteAllRecentSelectBrandTest() {
        //given
        User user = 카카오_유저_생성();
        user.changeNickname("deleteAllRecentSelectBrandTest");
        userRepository.save(user);

        Brand saveBrand = brandRepository.save(
                Brand.builder().brandKr("브랜드1").brandEn("brand1").brandImgUrl(null).build()
        );

        recentSelectBrandRepository.save(RecentSelectBrand.toEntity(saveBrand, null, user));

        //when
        recentSelectBrandService.deleteAllRecentSelectBrand(user);

        //then
        assertThat(recentSelectBrandRepository.findAll()).hasSize(0);
    }

    @DisplayName("최근 검색한 특정 브랜드를 삭제한다.")
    @Test
    void deleteRecentSelectBrandTest() {
        //given
        User user = 카카오_유저_생성();
        userRepository.save(user);

        Brand brand1 = Brand.builder().brandKr("브랜드1").brandEn("brand1").brandImgUrl(null).build();
        Brand brand2 = Brand.builder().brandKr("브랜드2").brandEn("brand2").brandImgUrl(null).build();

        List<Brand> brands = brandRepository.saveAll(List.of(brand1, brand2));

        RecentSelectBrand recentSelectBrand1 = RecentSelectBrand.toEntity(brands.get(0), null, user);
        RecentSelectBrand recentSelectBrand2 = RecentSelectBrand.toEntity(brands.get(0), null, user);
        RecentSelectBrand recentSelectBrand3 = RecentSelectBrand.toEntity(brands.get(1), null, user);

        recentSelectBrandRepository.saveAll(List.of(recentSelectBrand1, recentSelectBrand2, recentSelectBrand3));

        //when
        recentSelectBrandService.deleteRecentSelectBrand(user, brands.get(0).getId(), "Y");

        //then
        assertThat(recentSelectBrandRepository.findAll()).hasSize(1);
    }
}
