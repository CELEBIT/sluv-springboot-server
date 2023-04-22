package com.sluv.server.domain.brand.controller;

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
@RequestMapping("/app/newBrand")
public class NewBrandController {
    private NewBrandService newBrandService;


    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<Long>> postNewBrand(@RequestBody String newBrandName){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<Long>builder()
                        .result(newBrandService.postNewBrand(newBrandName))
                        .build()
        );
    }
}
