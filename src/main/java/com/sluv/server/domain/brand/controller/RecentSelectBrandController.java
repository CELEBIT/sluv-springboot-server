package com.sluv.server.domain.brand.controller;

import com.sluv.server.domain.brand.dto.RecentSelectBrandReqDto;
import com.sluv.server.domain.brand.dto.RecentSelectBrandResDto;
import com.sluv.server.domain.brand.service.BrandService;
import com.sluv.server.domain.brand.service.RecentSelectBrandService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/brand/recent")
public class RecentSelectBrandController {
    private final BrandService brandService;
    private final RecentSelectBrandService recentSelectBrandService;

    @Operation(
            summary = "최근 선택한 브랜드 검색",
            description = "최근 선택한 브랜드을 검색"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<List<RecentSelectBrandResDto>>> getRecentSelectBrand(@AuthenticationPrincipal User user){


        return ResponseEntity.ok()
                .body(
                        SuccessDataResponse.<List<RecentSelectBrandResDto>>builder()
                                .result(brandService.findRecentSelectBrand(user))
                                .build()
                );

    }
    @Operation(
            summary = "최근 선택한 브랜드 등록",
            description = "최근 선택한 브랜드을 등록"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postRecentSelectBrand(@AuthenticationPrincipal User user, @RequestBody RecentSelectBrandReqDto dto ){

        recentSelectBrandService.postRecentSelectBrand(user, dto);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
