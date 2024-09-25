package com.sluv.api.item.controller;

import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.item.dto.ItemCategoryParentResponseDto;
import com.sluv.api.item.service.ItemCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item/category")
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;

    @Operation(summary = "아이템 카테고리 조회", description = "상위 카테고리를 기준으로 묶음")
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<List<ItemCategoryParentResponseDto>>> getItemCategory() {

        List<ItemCategoryParentResponseDto> response = itemCategoryService.getItemCategory();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

}
