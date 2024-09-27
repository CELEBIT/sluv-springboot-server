package com.sluv.api.brand.controller;

import com.sluv.api.brand.dto.request.NewBrandPostRequest;
import com.sluv.api.brand.dto.response.NewBrandPostResponse;
import com.sluv.api.brand.service.NewBrandService;
import com.sluv.api.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
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

    private final NewBrandService newBrandService;

    @Operation(summary = "NewBrand 등록", description = "새로운 NewBrand를 등록한다.")
    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<NewBrandPostResponse>> postNewBrand(
            @RequestBody NewBrandPostRequest request) {
        NewBrandPostResponse response = newBrandService.postNewBrand(request);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

}
