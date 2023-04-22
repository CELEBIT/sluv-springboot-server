package com.sluv.server.domain.celeb.controller;

import com.sluv.server.domain.celeb.service.NewCelebService;
import com.sluv.server.global.common.response.SuccessDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/newCeleb")
public class NewCelebController {
    private NewCelebService newCelebService;


    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<Long>> postNewCeleb(@RequestBody String newCelebName){
        return ResponseEntity.ok().body(
                SuccessDataResponse.<Long>builder()
                        .result(newCelebService.postNewCeleb(newCelebName))
                        .build()
        );
    }
}
