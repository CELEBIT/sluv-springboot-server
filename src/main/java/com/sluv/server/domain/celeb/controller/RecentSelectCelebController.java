package com.sluv.server.domain.celeb.controller;

import com.sluv.server.domain.celeb.dto.RecentSelectCelebReqDto;
import com.sluv.server.domain.celeb.dto.RecentSelectCelebResDto;
import com.sluv.server.domain.celeb.service.CelebService;
import com.sluv.server.domain.celeb.service.RecentSelectCelebService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/app/celeb/recent")
public class RecentSelectCelebController {
    private final CelebService celebService;
    private final RecentSelectCelebService recentSelectCelebService;

    @Operation(summary = "*최근 선택한 Celeb 조회", description = "User 토큰 필요. 최근 선택한 20개 조회.")
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<List<RecentSelectCelebResDto>>> getRecentSelectCeleb(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<RecentSelectCelebResDto>>builder()
                        .result(celebService.getUserRecentSelectCeleb(user))
                        .build()
        );
    }

    @Operation(summary = "*최근 선택한 셀럽 등록", description = "User 토큰 필요.")
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postRecentSelectCeleb(@AuthenticationPrincipal User user,
                                                                 @RequestBody RecentSelectCelebReqDto dto) {

        recentSelectCelebService.postRecentSelectCeleb(user, dto);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*유저가 최근 선택한 셀럽 모두 삭제", description = "User 토큰 필요.")
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deleteAllRecentSelectCeleb(@AuthenticationPrincipal User user) {
        recentSelectCelebService.deleteAllRecentSelectCeleb(user);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*유저의 특정 [최근 선택한 셀럽] 삭제", description = "User 토큰 필요.")
    @DeleteMapping("/{celebId}")
    public ResponseEntity<SuccessResponse> deleteRecentSelectCeleb(@AuthenticationPrincipal User user,
                                                                   @PathVariable("celebId") Long celebId,
                                                                   @RequestParam("flag") String flag) {
        recentSelectCelebService.deleteRecentSelectCeleb(user, celebId, flag);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
