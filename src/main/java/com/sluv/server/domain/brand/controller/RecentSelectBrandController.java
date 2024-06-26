package com.sluv.server.domain.brand.controller;

import com.sluv.server.domain.brand.dto.RecentSelectBrandReqDto;
import com.sluv.server.domain.brand.dto.RecentSelectBrandResDto;
import com.sluv.server.domain.brand.service.BrandService;
import com.sluv.server.domain.brand.service.RecentSelectBrandService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<SuccessDataResponse<List<RecentSelectBrandResDto>>> getRecentSelectBrand(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<RecentSelectBrandResDto>>builder()
                        .result(brandService.findRecentSelectBrand(user))
                        .build()
        );

    }

    @Operation(summary = "*최근 선택한 브랜드 등록", description = "User 토큰 필요")
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postRecentSelectBrand(@AuthenticationPrincipal User user,
                                                                 @RequestBody RecentSelectBrandReqDto dto) {
        log.info("유저의 최근 선택한 브랜드 등록. 유저 : {}, Brand {}, NewBrand {}",
                user.getId(), dto.getBrandId(), dto.getNewBrandId());

        recentSelectBrandService.postRecentSelectBrand(user, dto);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*유저가 최근 선택한 브랜드 모두 삭제", description = "User 토큰 필요")
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deleteAllRecentSelectBrand(@AuthenticationPrincipal User user) {
        log.info("유저의 최근 선택한 브랜드 모두 삭제. 유저 : {}", user.getId());
        recentSelectBrandService.deleteAllRecentSelectBrand(user);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*유저의 특정 [최근 선택한 브랜드] 삭제", description = "User 토큰 필요")
    @DeleteMapping("/{brandId}")
    public ResponseEntity<SuccessResponse> deleteRecentSelectBrand(@AuthenticationPrincipal User user,
                                                                   @PathVariable("brandId") Long brandId,
                                                                   @RequestParam("flag") String flag) {
        log.info("유저의 최근 선택한 브랜드 삭제. 유저 : {}, 브랜드 : {}, 플래그 : {}", user.getId(), brandId, flag);
        recentSelectBrandService.deleteRecentSelectBrand(user, brandId, flag);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
