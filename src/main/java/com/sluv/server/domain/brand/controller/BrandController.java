package com.sluv.server.domain.brand.controller;

import com.sluv.server.domain.brand.dto.BrandSearchResDto;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.service.BrandService;
import com.sluv.server.global.common.response.SuccessDataResponse;
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
    @GetMapping("/search")
    public ResponseEntity<SuccessDataResponse<List<BrandSearchResDto>>> getBrandSearch(@RequestParam String brandName, Pageable pageable){


        return ResponseEntity.ok()
                .body(
                        SuccessDataResponse.<List<BrandSearchResDto>>builder()
                                            .result(brandService.findAllBrand(brandName, pageable))
                                            .build()
                );

    }
}
