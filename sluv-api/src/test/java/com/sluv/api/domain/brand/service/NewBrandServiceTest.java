package com.sluv.api.domain.brand.service;

import com.sluv.api.brand.dto.request.NewBrandPostRequest;
import com.sluv.api.brand.dto.response.NewBrandPostResponse;
import com.sluv.api.brand.service.NewBrandService;
import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.brand.service.NewBrandDomainService;
import com.sluv.infra.discord.WebHookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NewBrandServiceTest {

    @InjectMocks
    private NewBrandService newBrandService;


    @Mock
    private NewBrandDomainService newBrandDomainService;

    @Mock
    private WebHookService webHookService;


    @DisplayName("뉴브랜드 등록 - 신규 등록")
    @Test
    void postNewBrandTest() {
        //given
        NewBrandPostRequest newBrandPostReqDto = new NewBrandPostRequest("뉴브랜드");
        NewBrand newBrand = NewBrand.builder()
                .id(1L)
                .brandName("뉴브랜드")
                .build();

        when(newBrandDomainService.findByBrandName("뉴브랜드")).thenReturn(null);
        when(newBrandDomainService.saveNewBrand(any(NewBrand.class))).thenReturn(newBrand);
        doNothing().when(webHookService).sendCreateNewBrandMessage(any(NewBrand.class));


        //when
        NewBrandPostResponse response = newBrandService.postNewBrand(newBrandPostReqDto);

        //then
        assertThat(response.getNewBrandId()).isEqualTo(1L);
        assertThat(response.getNewBrandName()).isEqualTo("뉴브랜드");
        verify(webHookService).sendCreateNewBrandMessage(any(NewBrand.class));
    }

    @DisplayName("뉴브랜드 등록 - 이미 등록된 뉴브랜드")
    @Test
    void postNewBrandTest_Duplicate() {
        //given
        NewBrandPostRequest newBrandPostReqDto = new NewBrandPostRequest("뉴브랜드");
        NewBrand newBrand = NewBrand.builder()
                .id(1L)
                .brandName("뉴브랜드")
                .build();

        when(newBrandDomainService.findByBrandName("뉴브랜드")).thenReturn(newBrand);


        //when
        NewBrandPostResponse response = newBrandService.postNewBrand(newBrandPostReqDto);

        //then
        assertThat(response.getNewBrandId()).isEqualTo(1L);
        assertThat(response.getNewBrandName()).isEqualTo("뉴브랜드");
        verify(newBrandDomainService, never()).saveNewBrand(any(NewBrand.class));
        verify(webHookService, never()).sendCreateNewBrandMessage(any(NewBrand.class));
    }

}
