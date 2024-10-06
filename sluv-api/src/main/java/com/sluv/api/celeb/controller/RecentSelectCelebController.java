package com.sluv.api.celeb.controller;

import com.sluv.api.celeb.dto.request.RecentSelectCelebRequest;
import com.sluv.api.celeb.dto.response.RecentSelectCelebResponse;
import com.sluv.api.celeb.service.CelebService;
import com.sluv.api.celeb.service.RecentSelectCelebService;
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
@RequestMapping("/app/celeb/recent")
public class RecentSelectCelebController {

    private final CelebService celebService;
    private final RecentSelectCelebService recentSelectCelebService;

    @Operation(summary = "*최근 선택한 Celeb 조회", description = "User 토큰 필요. 최근 선택한 20개 조회.")
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<List<RecentSelectCelebResponse>>> getRecentSelectCeleb(
            @CurrentUserId Long userId) {
        List<RecentSelectCelebResponse> response = celebService.getUserRecentSelectCeleb(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*최근 선택한 셀럽 등록", description = "User 토큰 필요.")
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postRecentSelectCeleb(@CurrentUserId Long userId,
                                                                 @RequestBody RecentSelectCelebRequest request) {

        recentSelectCelebService.postRecentSelectCeleb(userId, request);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*유저가 최근 선택한 셀럽 모두 삭제", description = "User 토큰 필요.")
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deleteAllRecentSelectCeleb(@CurrentUserId Long userId) {
        recentSelectCelebService.deleteAllRecentSelectCeleb(userId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*유저의 특정 [최근 선택한 셀럽] 삭제", description = "User 토큰 필요.")
    @DeleteMapping("/{celebId}")
    public ResponseEntity<SuccessResponse> deleteRecentSelectCeleb(@CurrentUserId Long userId,
                                                                   @PathVariable("celebId") Long celebId,
                                                                   @RequestParam("flag") String flag) {
        recentSelectCelebService.deleteRecentSelectCeleb(userId, celebId, flag);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }
}
