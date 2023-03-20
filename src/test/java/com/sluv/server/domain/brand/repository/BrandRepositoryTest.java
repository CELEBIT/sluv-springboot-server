package com.sluv.server.domain.brand.repository;

import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.UserRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByAllBrandKrOrBrandEnStartingWith() throws Exception{
        //given
        Brand brand1 = Brand.builder()
                            .brandKr("폴로")
                            .brandEn("POLO")
                            .build();
        Brand brand2 = Brand.builder()
                            .brandKr("폴햄")
                            .brandEn("POLHAM")
                            .build();
        Brand brand3 = Brand.builder()
                            .brandKr("Push버튼")
                            .brandEn("ushButton")
                            .build();

        brandRepository.save(brand1);
        brandRepository.save(brand2);
        brandRepository.save(brand3);
        
        Pageable pageable1 = PageRequest.of(0, 2);
        Pageable pageable2 = PageRequest.of(1, 2);

        //when
        List<Brand> resultPage1 = brandRepository.findByAllBrandKrOrBrandEnStartingWith("P", pageable1).stream().toList();
        List<Brand> resultPage2 = brandRepository.findByAllBrandKrOrBrandEnStartingWith("P", pageable2).stream().toList();

        //then
//        assertThat(resultPage1).hasSize(2);
//        assertThat(resultPage2).hasSize(1);

    }
//    @AfterAll
//    public static void cleanUp(BrandRepository brandRepository) {
//        brandRepository.deleteAll();
//    }
    @Test
    public void findTop10ByTest() throws Exception{
        //given
            //skip
        //when
        List<Brand> result = brandRepository.findTop10By();

        //then
        assertThat(result).hasSize(9);

    }

    @Test
    public void findRecentByUserIdTest() throws Exception{
        //given
        User user = userRepository.findById(1L).orElse(null);
        Pageable pageable = PageRequest.of(0, 10);
        //when
        List<Brand> result = brandRepository.findRecentByUserId(user, pageable).stream().collect(Collectors.toList());

        //then
        assertThat(result).hasSize(9);

    }
}