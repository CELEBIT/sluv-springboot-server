//package com.sluv.server.domain.brand.service;
//
//import com.sluv.server.domain.brand.dto.NewBrandPostReqDto;
//import com.sluv.server.domain.brand.repository.NewBrandRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class NewBrandServiceTest {
//
//    @Autowired
//    private NewBrandService newBrandService;
//
//    @Autowired
//    private NewBrandRepository newBrandRepository;
//
//    @AfterEach
//    void clear() {
//        newBrandRepository.deleteAll();
//    }
//
//    @DisplayName("뉴브랜드를 저장한다.")
//    @Test
//    void postNewBrandTest() {
//        //given
//        NewBrandPostReqDto newBrandPostReqDto = new NewBrandPostReqDto("뉴브랜드1");
//
//        //when
//        newBrandService.postNewBrand(newBrandPostReqDto);
//
//        //then
//        Assertions.assertThat(newBrandRepository.findAll()).hasSize(1);
//    }
//
//}
