package com.sluv.api.brand.controller;

import com.sluv.api.brand.dto.request.RecentSelectBrandRequest;
import com.sluv.api.brand.dto.response.RecentSelectBrandResponse;
import com.sluv.api.brand.service.RecentSelectBrandService;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.common.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/brand/recent")
public class RecentSelectBrandController {
    private final RecentSelectBrandService recentSelectBrandService;

    @Operation(summary = "*최근 선택한 브랜드 검색", description = "최근 선택한 브랜드을 검색")
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<List<RecentSelectBrandResponse>>> getRecentSelectBrand(
            @CurrentUserId Long userId) {
        List<RecentSelectBrandResponse> response = recentSelectBrandService.findRecentSelectBrand(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "*최근 선택한 브랜드 등록", description = "User 토큰 필요")
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postRecentSelectBrand(@CurrentUserId Long userId,
                                                                 @RequestBody RecentSelectBrandRequest request) {
        recentSelectBrandService.postRecentSelectBrand(userId, request);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*유저가 최근 선택한 브랜드 모두 삭제", description = "User 토큰 필요")
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deleteAllRecentSelectBrand(@CurrentUserId Long userId) {
        recentSelectBrandService.deleteAllRecentSelectBrand(userId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*유저의 특정 [최근 선택한 브랜드] 삭제", description = "User 토큰 필요")
    @DeleteMapping("/{brandId}")
    public ResponseEntity<SuccessResponse> deleteRecentSelectBrand(@CurrentUserId Long userId,
                                                                   @PathVariable("brandId") Long brandId,
                                                                   @RequestParam("flag") String flag) {
        recentSelectBrandService.deleteRecentSelectBrand(userId, brandId, flag);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }
}
