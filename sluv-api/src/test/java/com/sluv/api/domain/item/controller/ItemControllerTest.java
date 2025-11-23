package com.sluv.api.domain.item.controller;

import com.sluv.api.item.controller.ItemController;
import com.sluv.api.item.dto.ItemDetailResDto;
import com.sluv.api.item.service.ItemService;
import com.sluv.domain.item.exception.ItemNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;


    @Test
    @DisplayName("아이템 상세조회")
    @WithMockUser("1")
    void getItemDetail() throws Exception {
        // given
        Long userId = 1L;
        Long itemId = 100L;

        ItemDetailResDto response = ItemDetailResDto.builder()
                        .itemName("test item")
                        .viewNum(100L)
                        .likeNum(50)
                        .build();

        when(itemService.getItemDetail(userId, itemId)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/app/item/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.itemName").value("test item"))
                .andExpect(jsonPath("$.result.viewNum").value(100L))
                .andExpect(jsonPath("$.result.likeNum").value(50));

    }

    @Test
    @DisplayName("비회원 아이템 상세조회")
    @WithMockUser(username = "anonymousUser")
    void getItemDetail_NonLogin() throws Exception {
        // given
        Long itemId = 100L;

        ItemDetailResDto response = ItemDetailResDto.builder()
                .itemName("test item")
                .viewNum(100L)
                .likeNum(50)
                .build();

        when(itemService.getItemDetail(null, itemId)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/app/item/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.itemName").value("test item"))
                .andExpect(jsonPath("$.result.viewNum").value(100L))
                .andExpect(jsonPath("$.result.likeNum").value(50));

    }

    @Test
    @DisplayName("존재하지 않는 아이템 조회 시 예외 발생")
    @WithMockUser(username = "anonymousUser")
    void getItemDetail_ItemNotFound() throws Exception {
        // given
        Long itemId = 999L;

        when(itemService.getItemDetail(null, itemId)).thenThrow(new ItemNotFoundException());

        // when & then
        mockMvc.perform(get("/app/item/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
