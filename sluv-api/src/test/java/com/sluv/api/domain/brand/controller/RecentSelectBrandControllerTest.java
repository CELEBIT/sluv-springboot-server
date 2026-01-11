package com.sluv.api.domain.brand.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluv.api.brand.controller.RecentSelectBrandController;
import com.sluv.api.brand.dto.request.RecentSelectBrandRequest;
import com.sluv.api.brand.dto.response.RecentSelectBrandResponse;
import com.sluv.api.brand.service.RecentSelectBrandService;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.RecentSelectBrand;
import com.sluv.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RecentSelectBrandController.class)
@ActiveProfiles("test")
public class RecentSelectBrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecentSelectBrandService recentSelectBrandService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("최근 선택한 브랜드 조회")
    @WithMockUser("1")
    void getRecentSelectBrandTest() throws Exception {
        // given
        Brand brand = Brand.of("나이키", "NIKE", "http://image.url");
        RecentSelectBrand recentSelectBrand = RecentSelectBrand.builder()
                .user(Mockito.mock(User.class))
                .brand(brand)
                .build();

        RecentSelectBrandResponse responseDto = RecentSelectBrandResponse.from(recentSelectBrand);
        List<RecentSelectBrandResponse> responseDtos = List.of(responseDto);

        when(recentSelectBrandService.findRecentSelectBrand(1L)).thenReturn(responseDtos);


        // when & then
        mockMvc.perform(get("/app/brand/recent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result[0].brandName").value("나이키"))
                .andExpect(jsonPath("$.result[0].brandImgUrl").value("http://image.url"));
    }

    @Test
    @DisplayName("최근 선택한 브랜드 등록")
    @WithMockUser("1")
    void postRecentSelectBrandTest() throws Exception {
        // given
        RecentSelectBrandRequest requestDto = new RecentSelectBrandRequest(1L, null);

        doNothing().when(recentSelectBrandService).postRecentSelectBrand(eq(1L), any(RecentSelectBrandRequest.class));

        // when & then
        mockMvc.perform(post("/app/brand/recent")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true));
    }

    @Test
    @DisplayName("최근 선택한 브랜드 모두 삭제")
    @WithMockUser("1")
    void deleteAllRecentSelectBrandTest() throws Exception {
        // given
        doNothing().when(recentSelectBrandService).deleteAllRecentSelectBrand(eq(1L));

        // when & then
        mockMvc.perform(delete("/app/brand/recent")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true));
    }

}
