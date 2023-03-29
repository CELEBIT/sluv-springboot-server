package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.ItemCategoryParentResponseDto;
import com.sluv.server.domain.item.service.ItemCategoryService;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "아이템 카테고리 조회",
            description = "상위 카테고리를 기준으로 묶음"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<List<ItemCategoryParentResponseDto>>> getItemCategory(){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<ItemCategoryParentResponseDto>>builder()
                        .result(itemCategoryService.getItemCategory())
                        .build()
        );
    }

}
