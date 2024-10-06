package com.sluv.admin.brand.controller;

import com.sluv.admin.brand.dto.HotBrandResDto;
import com.sluv.admin.brand.service.BrandService;
import com.sluv.admin.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/backoffice/item/dashBoard")
public class BrandDashBoardController {

    private final BrandService brandService;

    @Operation(
            summary = "대시보드 - 인기 사용 브랜드 조회",
            description = "대시보드에서 가장 많이 사용된 브랜드 Top 3을 조회한다."
    )
    @GetMapping("/hotBrand")
    public ResponseEntity<SuccessDataResponse<List<HotBrandResDto>>> getHotBrand() {
        List<HotBrandResDto> response = brandService.getTop3HotBrand();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }
}
