package com.sluv.api.celeb.controller;

import com.sluv.api.celeb.dto.response.CelebActivityResponse;
import com.sluv.api.celeb.service.CelebActivityService;
import com.sluv.api.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/celeb/activity")
public class CelebActivityController {

    private final CelebActivityService celebActivityService;

    @Operation(summary = "셀럽의 활동 목록 조회", description = "중복 이름을 가진 셀럽을 분류할 때 사용. [최신순으로 8개를 조회]")
    @GetMapping("/{celebId}")
    public ResponseEntity<SuccessDataResponse<List<CelebActivityResponse>>> getCelebActivity(
            @PathVariable("celebId") Long celebId) {
        List<CelebActivityResponse> response = celebActivityService.getCelebActivity(celebId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

}
