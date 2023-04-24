package com.sluv.server.domain.celeb.controller;

import com.sluv.server.domain.celeb.dto.RecentSelectCelebResDto;
import com.sluv.server.domain.celeb.dto.RecentSelectCelebReqDto;
import com.sluv.server.domain.celeb.service.CelebService;
import com.sluv.server.domain.celeb.service.RecentSelectCelebService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/celeb/recent")
public class RecentSelectCelebController {
    private final CelebService celebService;
    private final RecentSelectCelebService recentSelectCelebService;

    @Operation(
            summary = "*최근 선택한 Celeb 조회",
            description = "유저가 최근 선택한 20개의 Celeb 조회",
            parameters = {@Parameter(name = "X-AUTH-TOKEN", required = true, description = "Authentication token", in = ParameterIn.HEADER)}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<List<RecentSelectCelebResDto>>> getRecentSelectCeleb(@AuthenticationPrincipal User user){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<RecentSelectCelebResDto>>builder()
                        .result(celebService.getUserRecentSelectCeleb(user))
                        .build()
        );
    }

    @Operation(
            summary = "*최근 선택한 셀럽 등록",
            description = "최근 선택한 셀럽을 등록"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    public ResponseEntity<SuccessResponse> postRecentSelectCeleb(@AuthenticationPrincipal User user, @RequestBody RecentSelectCelebReqDto dto ){

        recentSelectCelebService.postRecentSelectCeleb(user, dto);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
    @Operation(
            summary = "*유저가 최근 선택한 셀럽 모두 삭제",
            description = "유저가 최근 선택한 셀럽 모두 삭제 API"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deleteAllRecentSelectCeleb(@AuthenticationPrincipal User user){
        recentSelectCelebService.deleteAllRecentSelectCeleb(user);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @DeleteMapping("/{celebId}")
    public ResponseEntity<SuccessResponse> deleteRecentSelectCeleb(@AuthenticationPrincipal User user, @PathVariable("celebId") Long celebId, @RequestParam("flag") String flag){
        recentSelectCelebService.deleteRecentSelectCeleb(user, celebId, flag);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
