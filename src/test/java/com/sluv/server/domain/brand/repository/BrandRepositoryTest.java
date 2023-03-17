package com.sluv.server.domain.brand.repository;

import com.sluv.server.domain.brand.dto.BrandSearchResDto;
import com.sluv.server.domain.brand.entity.Brand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void findAllByBrandKrStartingWith() throws Exception{
        //given
        Brand brand1 = Brand.builder()
                            .brandKr("폴로")
                            .brandEn("POLO")
                            .build();
        Brand brand2 = Brand.builder()
                            .brandKr("폴햄")
                            .brandEn("POLHAM")
                            .build();

        brandRepository.save(brand1);
        brandRepository.save(brand2);

        //when
//        List<BrandSearchResDto> result = brandRepository.findAllByBrandKrStartingWith("폴");

        //then
//        assertThat(result).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        brandRepository.deleteAll();
    }
}