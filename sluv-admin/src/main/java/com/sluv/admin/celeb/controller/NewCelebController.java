package com.sluv.admin.celeb.controller;

import com.sluv.admin.celeb.dto.NewCelebChangeRequest;
import com.sluv.admin.celeb.dto.NewCelebSelfPostRequest;
import com.sluv.admin.celeb.service.NewCelebService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/new-celeb")
@RequiredArgsConstructor
public class NewCelebController {

    private final NewCelebService newCelebService;

    @PostMapping("/change")
    public ResponseEntity<Void> changeNewCeleb(@RequestBody NewCelebChangeRequest request) {
        newCelebService.changeNewCelebToCeleb(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/self")
    public ResponseEntity<String> selfPostNewCeleb(@RequestBody NewCelebSelfPostRequest request) {
        newCelebService.registerNewCelebToCeleb(request);
        return ResponseEntity.ok().body("hi");
    }
}
