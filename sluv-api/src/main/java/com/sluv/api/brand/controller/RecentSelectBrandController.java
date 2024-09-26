package com.sluv.api.brand.controller;

import com.sluv.api.brand.dto.request.RecentSelectBrandRequest;
import com.sluv.api.brand.dto.response.RecentSelectBrandResponse;
import com.sluv.api.brand.service.BrandService;
import com.sluv.api.brand.service.RecentSelectBrandService;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.common.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/app/brand/recent")
public class RecentSelectBrandController {
    private final BrandService brandService;
    private final RecentSelectBrandService recentSelectBrandService;

    @Operation(summary = "*최근 선택한 브랜드 검색", description = "최근 선택한 브랜드을 검색")
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<List<RecentSelectBrandResponse>>> getRecentSelectBrand(
            @CurrentUserId Long userId) {
        List<RecentSelectBrandResponse> response = brandService.findRecentSelectBrand(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*최근 선택한 브랜드 등록", description = "User 토큰 필요")
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postRecentSelectBrand(@CurrentUserId Long userId,
                                                                 @RequestBody RecentSelectBrandRequest request) {
        log.info("유저의 최근 선택한 브랜드 등록. 유저 : {}, Brand {}, NewBrand {}",
                userId, request.getBrandId(), request.getNewBrandId());
        recentSelectBrandService.postRecentSelectBrand(userId, request);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*유저가 최근 선택한 브랜드 모두 삭제", description = "User 토큰 필요")
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deleteAllRecentSelectBrand(@CurrentUserId Long userId) {
        log.info("유저의 최근 선택한 브랜드 모두 삭제. 유저 : {}", userId);
        recentSelectBrandService.deleteAllRecentSelectBrand(userId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*유저의 특정 [최근 선택한 브랜드] 삭제", description = "User 토큰 필요")
    @DeleteMapping("/{brandId}")
    public ResponseEntity<SuccessResponse> deleteRecentSelectBrand(@CurrentUserId Long userId,
                                                                   @PathVariable("brandId") Long brandId,
                                                                   @RequestParam("flag") String flag) {
        log.info("유저의 최근 선택한 브랜드 삭제. 유저 : {}, 브랜드 : {}, 플래그 : {}", userId, brandId, flag);
        recentSelectBrandService.deleteRecentSelectBrand(userId, brandId, flag);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }
}
