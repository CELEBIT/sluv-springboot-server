package com.sluv.server.domain.brand.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sluv.server.domain.brand.entity.Brand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class BrandRepositoryTest {
    @Autowired
    private BrandRepository brandRepository;

    @AfterEach
    void tearDown() {
        brandRepository.deleteAllInBatch();
    }

    @DisplayName("한글 혹은 영어 이름으로 브랜드를 검색할 수 있다.")
    @Test
    void findByAllBrandKrOrBrandEnStartingWith() {
        // given
        Brand brand1 = Brand.builder().brandKr("테스트1").brandEn("test1").build();
        Brand brand2 = Brand.builder().brandKr("테스트2").brandEn("test2").build();

        brandRepository.save(brand1);
        brandRepository.save(brand2);

        PageRequest pageable = PageRequest.of(0, 2);

        // when
        Page<Brand> brandPage = brandRepository.findByAllBrandKrOrBrandEnStartingWith("테스트", pageable);

        // then
        assertThat(brandPage.getContent().size()).isEqualTo(2);
        assertThat(brandPage.getContent().get(0).getBrandKr()).isEqualTo("테스트1");

    }
}