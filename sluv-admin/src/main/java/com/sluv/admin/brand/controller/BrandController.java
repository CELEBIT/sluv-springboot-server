package com.sluv.admin.brand.controller;

import com.sluv.admin.brand.dto.BrandRegisterRequest;
import com.sluv.admin.brand.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping("/self")
    public ResponseEntity<Void> saveBrand(@RequestBody BrandRegisterRequest request) {
        brandService.saveBrand(request);
        return ResponseEntity.ok().build();
    }
}
