package com.sluv.server.domain.brand.controller;

import com.sluv.server.domain.brand.dto.NewBrandPostReqDto;
import com.sluv.server.domain.brand.dto.NewBrandPostResDto;
import com.sluv.server.domain.brand.service.NewBrandService;
import com.sluv.server.global.common.response.SuccessDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/app/newBrand")
public class NewBrandController {
    private final NewBrandService newBrandService;


    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<NewBrandPostResDto>> postNewBrand(@RequestBody NewBrandPostReqDto dto) {
        log.info("NewBrand 등록. NewBrand Name:{}", dto.getNewBrandName());
        return ResponseEntity.ok().body(
                SuccessDataResponse.<NewBrandPostResDto>builder()
                        .result(newBrandService.postNewBrand(dto))
                        .build()
        );
    }
}
