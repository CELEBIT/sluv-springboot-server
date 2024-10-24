package com.sluv.admin.celeb.controller;

import com.sluv.admin.celeb.dto.CelebSelfPostRequest;
import com.sluv.admin.celeb.service.CelebService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/celeb")
@RequiredArgsConstructor
public class CelebController {

    private final CelebService celebService;

    @PostMapping("/self")
    public ResponseEntity<Void> saveCeleb(@RequestBody CelebSelfPostRequest request) {
        System.out.println(request.toString());
        celebService.saveCeleb(request);
        return ResponseEntity.ok().build();
    }
}
