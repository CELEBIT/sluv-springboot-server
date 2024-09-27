package com.sluv.api.celeb.controller;

import com.sluv.api.celeb.dto.request.NewCelebPostRequest;
import com.sluv.api.celeb.dto.response.NewCelebPostResponse;
import com.sluv.api.celeb.service.NewCelebService;
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
@RequestMapping("/app/newCeleb")
public class NewCelebController {

    private final NewCelebService newCelebService;

    @Operation(summary = "NewCeleb 등록", description = "사용자가 새로운 NewCeleb 등록")
    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<NewCelebPostResponse>> postNewCeleb(
            @RequestBody NewCelebPostRequest request) {
        NewCelebPostResponse response = newCelebService.postNewCeleb(request);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

}
