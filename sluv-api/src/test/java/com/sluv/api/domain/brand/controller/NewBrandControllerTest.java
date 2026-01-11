package com.sluv.api.domain.brand.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluv.api.brand.controller.NewBrandController;
import com.sluv.api.brand.dto.request.NewBrandPostRequest;
import com.sluv.api.brand.dto.request.RecentSelectBrandRequest;
import com.sluv.api.brand.dto.response.NewBrandPostResponse;
import com.sluv.api.brand.service.NewBrandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NewBrandController.class)
@ActiveProfiles("test")
public class NewBrandControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewBrandService newBrandService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("뉴브랜드 등록")
    @WithMockUser("1")
    void postNewBrandTest() throws Exception {
        // given
        NewBrandPostRequest requestDto = new NewBrandPostRequest("뉴브랜드");
        NewBrandPostResponse response = NewBrandPostResponse.builder()
                .newBrandId(1L)
                .newBrandName("뉴브랜드")
                .build();

        when(newBrandService.postNewBrand(any(NewBrandPostRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/app/newBrand")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.newBrandName").value("뉴브랜드"));
    }

}
