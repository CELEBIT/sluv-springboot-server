package com.sluv.api.brand.controller;

import com.sluv.api.brand.dto.response.BrandSearchResponse;
import com.sluv.api.brand.service.BrandService;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/brand")
public class BrandController {
    private final BrandService brandService;

    @Operation(summary = "브랜드 검색", description = "브랜드 검색(Pagination)")
    @GetMapping("/search")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<BrandSearchResponse>>> getBrandSearch(
            @RequestParam String brandName, Pageable pageable) {
        System.out.println("brandName = " + brandName + ", pageable = " + pageable);
        PaginationResponse<BrandSearchResponse> response = brandService.findAllBrand(brandName, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "인기 브랜드 검색", description = "인기 브랜드 검색(상위 10개)")
    @GetMapping("/top")
    public ResponseEntity<SuccessDataResponse<List<BrandSearchResponse>>> getTopBrand() {
        List<BrandSearchResponse> response = brandService.findTopBrand();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

}
