package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.ItemCategoryParentResponseDto;
import com.sluv.server.domain.item.service.ItemCategoryService;
import com.sluv.server.global.common.response.SuccessDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item/category")
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;

    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<List<ItemCategoryParentResponseDto>>> getItemCategory(){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<ItemCategoryParentResponseDto>>builder()
                        .result(itemCategoryService.getItemCategory())
                        .build()
        );
    }

}
