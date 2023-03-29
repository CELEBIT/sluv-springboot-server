package com.sluv.server.domain.celeb.controller;

import com.sluv.server.domain.celeb.dto.CelebSearchResDto;
import com.sluv.server.domain.celeb.service.CelebService;
import com.sluv.server.global.common.response.SuccessDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/celeb")
public class CelebController {
    private final CelebService celebService;

    @GetMapping("/search")
    public ResponseEntity<SuccessDataResponse<List<CelebSearchResDto>>> searchCelebByName(@RequestParam String celebName, Pageable pageable){


        return ResponseEntity.ok().body(
            SuccessDataResponse.<List<CelebSearchResDto>>builder()
                    .result(celebService.searchCeleb(celebName, pageable))
                    .build()
        );
    }
}
