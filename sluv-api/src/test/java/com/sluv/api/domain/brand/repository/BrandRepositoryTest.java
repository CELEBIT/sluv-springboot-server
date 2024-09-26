//package com.sluv.server.domain.brand.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.sluv.server.domain.auth.enums.SnsType;
//import com.sluv.server.domain.brand.entity.Brand;
//import com.sluv.server.domain.brand.entity.RecentSelectBrand;
//import com.sluv.server.domain.user.entity.User;
//import com.sluv.server.domain.user.enums.UserStatus;
//import com.sluv.server.domain.user.repository.UserRepository;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//
//@SpringBootTest
//class BrandRepositoryTest {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private BrandRepository brandRepository;
//    @Autowired
//    private RecentSelectBrandRepository recentSelectBrandRepository;
//
//    @AfterEach
//    void tearDown() {
//        recentSelectBrandRepository.deleteAll();
//        brandRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    @DisplayName("한글 혹은 영어 이름으로 브랜드를 검색할 수 있다.")
//    @Test
//    void findByAllBrandKrOrBrandEnStartingWith() {
//        // given
//        Brand brand1 = Brand.builder().brandKr("테스트1").brandEn("test1").build();
//        Brand brand2 = Brand.builder().brandKr("테스트2").brandEn("test2").build();
//
//        brandRepository.save(brand1);
//        brandRepository.save(brand2);
//
//        PageRequest pageable = PageRequest.of(0, 2);
//
//        // when
//        Page<Brand> brandPage = brandRepository.findByAllBrandKrOrBrandEnStartingWith("테스트", pageable);
//
//        // then
//        assertThat(brandPage.getContent().size()).isEqualTo(2);
//        assertThat(brandPage.getContent().get(0).getBrandKr()).isEqualTo("테스트1");
//
//    }
//
//    @DisplayName("최근 많이 검색된 브랜드 상위 10개를 검색한다.")
//    @Test
//    void findTop10Brand() {
//        // given
//        List<Brand> brands = new ArrayList<>();
//        List<RecentSelectBrand> recentSelectBrands = new ArrayList<>();
//        for (int i = 1; i <= 11; i++) {
//            brands.add(Brand.builder().brandKr("테스트" + i).brandEn("test" + i).build());
//        }
//        brandRepository.saveAll(brands);
//
//        User user = User.builder()
//                .email("testMan@sluv.com")
//                .snsType(SnsType.KAKAO)
//                .userStatus(UserStatus.ACTIVE)
//                .build();
//
//        User saveUser = userRepository.save(user);
//
//        for (int j = 0; j < brands.size(); j++) {
//            for (int i = 1; i <= j + 1; i++) {
//                recentSelectBrands.add(RecentSelectBrand.builder()
//                        .brand(brands.get(j))
//                        .user(saveUser)
//                        .build()
//                );
//            }
//        }
//
//        recentSelectBrandRepository.saveAll(recentSelectBrands);
//
//        // when
//        List<Brand> top10By = brandRepository.findTop10By();
//
//        // then
//        assertThat(top10By.get(0).getBrandKr()).isEqualTo("테스트11");
//        assertThat(top10By.get(1).getBrandKr()).isEqualTo("테스트10");
//        assertThat(top10By.size()).isEqualTo(10);
//
//    }
//}