package com.sluv.domain.brand.repository;

import com.sluv.domain.auth.enums.SnsType;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.RecentSelectBrand;
import com.sluv.domain.config.TestConfig;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserAge;
import com.sluv.domain.user.enums.UserGender;
import com.sluv.domain.user.enums.UserStatus;
import com.sluv.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private RecentSelectBrandRepository recentSelectBrandRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        recentSelectBrandRepository.deleteAll();
        userRepository.deleteAll();
        brandRepository.deleteAll();
    }

    @Test
    @DisplayName("한글 키워드로 브랜드 조회")
    void findByAllWithKrNameTest() {
        // given
        String searchKeyword = "나";
        PageRequest pageRequest = PageRequest.of(0, 1);

        Brand brand = Brand.builder()
                .brandKr("나이키")
                .brandEn("NIKE")
                .brandImgUrl("http://image.ulr")
                .build();
        brandRepository.save(brand);

        // when
        Page<Brand> brandPage = brandRepository.findByAllBrandKrOrBrandEnStartingWith(searchKeyword, pageRequest);

        // then
        assertThat(brandPage.getContent().size()).isEqualTo(1);
        assertThat(brandPage.getContent().get(0).getBrandKr()).isEqualTo("나이키");
    }

    @Test
    @DisplayName("영어 키워드로 브랜드 조회")
    void findByAllWithEnNameTest() {
        // given
        String searchKeyword = "N";
        PageRequest pageRequest = PageRequest.of(0, 1);

        Brand brand = Brand.builder()
                .brandKr("나이키")
                .brandEn("NIKE")
                .brandImgUrl("http://image.ulr")
                .build();
        brandRepository.save(brand);

        // when
        Page<Brand> brandPage = brandRepository.findByAllBrandKrOrBrandEnStartingWith(searchKeyword, pageRequest);

        // then
        assertThat(brandPage.getContent().size()).isEqualTo(1);
        assertThat(brandPage.getContent().get(0).getBrandEn()).isEqualTo("NIKE");
    }

    @Test
    @DisplayName("Top 10 브랜드 조회")
    void findTop10ByTest() {
        // given
        User user1 = User.builder()
                .email("user1@example.com")
                .snsType(SnsType.ETC)
                .nickname("u1")
                .userStatus(UserStatus.ACTIVE)
                .ageRange(UserAge.TWENTIES)
                .gender(UserGender.FEMALE)
                .build();
        userRepository.save(user1);

        List<Brand> brands = new ArrayList<>();
        for (int i=1; i<=11; i++) {
            Brand brand = Brand.builder()
                    .brandKr("나이키"+i)
                    .brandEn("NIKE"+i)
                    .brandImgUrl("http://image.ulr")
                    .build();
            brands.add(brand);
        }
        brandRepository.saveAll(brands);


        List<RecentSelectBrand> recentSelectBrands = new ArrayList<>();
        for (int i=1; i<=10; i++) {
            RecentSelectBrand recentSelectBrand = RecentSelectBrand.builder()
                    .brand(brands.get(i-1))
                    .user(user1)
                    .build();
            recentSelectBrands.add(recentSelectBrand);
        }

        // 2번 브랜드를 1등으로 설정
        RecentSelectBrand recentSelectBrand = RecentSelectBrand.builder()
                .brand(brands.get(1))
                .user(user1)
                .build();
        recentSelectBrands.add(recentSelectBrand);
        recentSelectBrandRepository.saveAll(recentSelectBrands);

        // when
        List<Brand> top10Brand = brandRepository.findTop10By();

        // then
        assertThat(top10Brand.size()).isEqualTo(10);
        assertThat(top10Brand.get(0).getBrandKr()).isEqualTo("나이키2");
    }

}
