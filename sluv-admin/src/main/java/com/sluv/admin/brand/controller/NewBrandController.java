package com.sluv.admin.brand.controller;

import com.sluv.admin.brand.dto.NewBrandChangeRequest;
import com.sluv.admin.brand.dto.NewBrandRegisterRequest;
import com.sluv.admin.brand.service.NewBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/new-brand")
@RequiredArgsConstructor
public class NewBrandController {

    private final NewBrandService newBrandService;


    @PostMapping("/change")
    public ResponseEntity<Void> changeNewBrand(@RequestBody NewBrandChangeRequest request) {
        newBrandService.changeNewBrandToBrand(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/self")
    public ResponseEntity<Void> registerNewBrand(@RequestBody NewBrandRegisterRequest request) {
        newBrandService.registerNewBrandToBrand(request);
        return ResponseEntity.ok().build();
    }
}
