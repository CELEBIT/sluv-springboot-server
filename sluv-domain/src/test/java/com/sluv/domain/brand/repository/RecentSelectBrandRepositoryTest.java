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
public class RecentSelectBrandRepositoryTest {

    @Autowired
    private RecentSelectBrandRepository recentSelectBrandRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandRepository brandRepository;

    @AfterEach
    void clean() {
        recentSelectBrandRepository.deleteAll();
        userRepository.deleteAll();
        brandRepository.deleteAll();
    }

    @Test
    @DisplayName("최근 선택한 브랜드 20개 조회")
    void getRecentSelectBrandTop20() {
        // given
        User user = User.builder()
                .email("user1@example.com")
                .snsType(SnsType.ETC)
                .nickname("u1")
                .userStatus(UserStatus.ACTIVE)
                .ageRange(UserAge.TWENTIES)
                .gender(UserGender.FEMALE)
                .build();
        userRepository.save(user);

        List<Brand> brands = new ArrayList<>();
        for (int i=1; i<=21; i++) {
            Brand brand = Brand.builder()
                    .brandKr("나이키"+i)
                    .brandEn("NIKE"+i)
                    .brandImgUrl("http://image.url")
                    .build();
            brands.add(brand);
        }
        brandRepository.saveAll(brands);


        List<RecentSelectBrand> recentSelectBrands = new ArrayList<>();
        for (int i=1; i<=20; i++) {
            RecentSelectBrand recentSelectBrand = RecentSelectBrand.builder()
                    .brand(brands.get(i-1))
                    .user(user)
                    .build();
            recentSelectBrands.add(recentSelectBrand);
        }

        recentSelectBrandRepository.saveAll(recentSelectBrands);

        // 21번 브랜드를 가장 최근에 선택
        RecentSelectBrand mostRecentSelectBrand = RecentSelectBrand.builder()
                .brand(brands.get(20))
                .user(user)
                .build();
        recentSelectBrandRepository.save(mostRecentSelectBrand);


        // when
        List<RecentSelectBrand> recentSelectBrandTop20 = recentSelectBrandRepository.getRecentSelectBrandTop20(user);

        // then
        assertThat(recentSelectBrandTop20.size()).isEqualTo(20);
        assertThat(recentSelectBrandTop20.get(0).getBrand().getBrandKr()).isEqualTo("나이키21");
    }

}
