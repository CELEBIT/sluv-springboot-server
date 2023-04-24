package com.sluv.server.domain.brand.controller;

import com.sluv.server.domain.brand.dto.RecentBrandReqDto;
import com.sluv.server.domain.brand.dto.RecentBrandResDto;
import com.sluv.server.domain.brand.service.BrandService;
import com.sluv.server.domain.brand.service.RecentBrandService;
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
@RequestMapping("/app/recentBrand")
public class RecentBrandController {
    private final BrandService brandService;
    private final RecentBrandService recentBrandService;

    @Operation(
            summary = "최근 검색한 브랜드",
            description = "최근 검색한 브랜드"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<List<RecentBrandResDto>>> getRecentSearchBrand(@AuthenticationPrincipal User user){


        return ResponseEntity.ok()
                .body(
                        SuccessDataResponse.<List<RecentBrandResDto>>builder()
                                .result(brandService.findRecentBrand(user))
                                .build()
                );

    }
    @Operation(
            summary = "최근 검색한 브랜드",
            description = "최근 검색한 브랜드"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postRecentBrand(@AuthenticationPrincipal User user, @RequestBody RecentBrandReqDto dto ){

        recentBrandService.postRecentBrand(user, dto);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
