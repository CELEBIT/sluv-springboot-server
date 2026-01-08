package com.sluv.domain.brand.repository;

import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.config.TestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @AfterEach
    void clean() {
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

}
