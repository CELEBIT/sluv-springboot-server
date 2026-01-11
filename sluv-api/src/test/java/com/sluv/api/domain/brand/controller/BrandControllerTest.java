package com.sluv.api.domain.brand.controller;

import com.sluv.api.brand.controller.BrandController;
import com.sluv.api.brand.dto.response.BrandSearchResponse;
import com.sluv.api.brand.service.BrandService;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.domain.brand.entity.Brand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BrandController.class)
@ActiveProfiles("test")
public class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService brandService;


    @Test
    @DisplayName("브랜드 검색")
    @WithMockUser("1")
    void getBrandSearchTest() throws Exception {
        // given
        String searchKeyword = "나";
        String brandKrName = "나이키";
        String brandEnName = "NIKE";
        String brandImageUrl = "http://image.url";
        PageRequest pageable = PageRequest.of(0, 1);
        Brand brand = Brand.of(brandKrName, brandEnName, brandImageUrl);
        List<Brand> content = List.of(brand);
        Page<Brand> brandPage = new PageImpl<>(content, pageable, content.size());


        BrandSearchResponse responseDto = BrandSearchResponse.from(brand);
        List<BrandSearchResponse> responseDtos = List.of(responseDto);
        PaginationResponse<BrandSearchResponse> response = PaginationResponse.of(brandPage, responseDtos);

        when(brandService.findAllBrand(searchKeyword, pageable)).thenReturn(response);


        // when & then
        mockMvc.perform(get("/app/brand/search")
                        .param("brandName", searchKeyword)
                        .param("size", "1")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.content[0].brandKr").value(brandKrName))
                .andExpect(jsonPath("$.result.content[0].brandEn").value(brandEnName))
                .andExpect(jsonPath("$.result.content[0].brandImgUrl").value(brandImageUrl));
    }

    @Test
    @DisplayName("Top 10 브랜드 검색")
    @WithMockUser("1")
    void getTopBrandTest() throws Exception {
        // given
        List<BrandSearchResponse> content = new ArrayList<>();
        for (int i=1; i<=10; i++) {
            Brand brand = Brand.of("나이키" + i, "NIKE" + i, "http://image.url");
            content.add(BrandSearchResponse.from(brand));
        }

        when(brandService.findTopBrand()).thenReturn(content);

        // when & then
        mockMvc.perform(get("/app/brand/top")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result", hasSize(10)));
    }

}
