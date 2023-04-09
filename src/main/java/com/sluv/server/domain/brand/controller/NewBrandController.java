package com.sluv.server.domain.brand.controller;

import com.sluv.server.domain.brand.dto.NewBrandPostReqDto;
import com.sluv.server.domain.brand.dto.NewBrandPostResDto;
import com.sluv.server.domain.brand.service.NewBrandService;
import com.sluv.server.global.common.response.SuccessDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/newbrand")
public class NewBrandController {

    private final NewBrandService newBrandService;

    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<NewBrandPostResDto>> postNewBrand(@RequestBody NewBrandPostReqDto dto){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<NewBrandPostResDto>builder()
                        .result(newBrandService.postNewBrand(dto))
                        .build()
                );
    }
}
