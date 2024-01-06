package com.sluv.server.domain.brand.controller;

import com.sluv.server.domain.brand.dto.BrandSearchResDto;
import com.sluv.server.domain.brand.service.BrandService;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/brand")
public class BrandController {
    private final BrandService brandService;

    @Operation(summary = "브랜드 검색", description = "브랜드 검색(Pagination)")
    @GetMapping("/search")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<BrandSearchResDto>>> getBrandSearch(
            @RequestParam String brandName, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<BrandSearchResDto>>builder()
                        .result(brandService.findAllBrand(brandName, pageable))
                        .build()
        );

    }

    @Operation(
            summary = "인기 브랜드 검색",
            description = "인기 브랜드 검색(상위 10개)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/top")
    public ResponseEntity<SuccessDataResponse<List<BrandSearchResDto>>> getTopBrand() {

        return ResponseEntity.ok()
                .body(
                        SuccessDataResponse.<List<BrandSearchResDto>>builder()
                                .result(brandService.findTopBrand())
                                .build()
                );

    }
}
