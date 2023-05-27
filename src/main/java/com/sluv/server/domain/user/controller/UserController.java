package com.sluv.server.domain.user.controller;


import com.sluv.server.domain.celeb.dto.InterestedCelebParentResDto;
import com.sluv.server.domain.celeb.dto.InterestedCelebPostReqDto;
import com.sluv.server.domain.item.dto.ItemEditReqDto;
import com.sluv.server.domain.item.service.ItemEditReqService;
import com.sluv.server.domain.user.dto.UserProfileReqDto;
import com.sluv.server.domain.user.dto.UserReportReqDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.service.UserReportService;
import com.sluv.server.domain.user.service.UserService;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/app/user")
public class UserController {
    private final UserService userService;
    private final UserReportService userReportService;

    @Operation(
            summary = "*유저의 관심 샐럽 조회",
            description = "유저를 기준으로 InterstedCeleb 테이블에서 일치하는 Celeb을 검색"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/celeb")
    public ResponseEntity<SuccessDataResponse<List<InterestedCelebParentResDto>>> getInterestedCeleb(@AuthenticationPrincipal User user){

        return ResponseEntity.ok().body(SuccessDataResponse.<List<InterestedCelebParentResDto>>builder()
                                                            .result(userService.getInterestedCeleb(user))
                                                            .build());
    }

    @Operation(
            summary = "*유저 팔로우/팔로잉",
            description = "유저의 팔로우/팔로잉 등록" +
                    "\n (User Id Token 필요)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{userId}/follow")
    public ResponseEntity<SuccessResponse> postUserFollow(@AuthenticationPrincipal User user, @PathVariable(name = "userId") Long userId) {
        userService.postUserFollow(user, userId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
    @Operation(
            summary = "*유저 신고하기",
            description = "유저를 신고하는 기능" +
                    "\n (User Id 필요)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{userId}/report")
    public ResponseEntity<SuccessResponse> postUserReport(@AuthenticationPrincipal User user, @PathVariable(name = "userId") Long userId, @RequestBody UserReportReqDto dto) {
        userReportService.postUserReport(user, userId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*유저의 관심 셀럽 업데이트",
            description = "유저의 관심 셀럽 목록을 업데이트" +
                    "\n 1. User Id를 기준으로 InterestedCeleb 테이블의 모든 데이터 삭제" +
                    "\n 2. User Id와 Dto의 정보를 바탕으로 정보 push" +
                    "\n (User Id Token 필요)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/celeb")
    public ResponseEntity<SuccessResponse> postInterestedCeleb(@AuthenticationPrincipal User user, @RequestBody InterestedCelebPostReqDto dto){
        userService.postInterestedCeleb(user, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*유저의 프로필 설정 및 수정",
            description = "최초 회원 가입 시 유저의 프로필 설정 및 추후 유저의 프로필 수정" +
                    "\n User의 Status가 \"PENDING_PROFILE\" 일 경우 설정 후 Status를 \"PENDING_CELEB\"로 변경" +
                    "\n User의 Status가 \"ACTIVE\" 일 경우 프로필 정보만 수정" +
                    "\n (User Id Token 필요)" +
                    "\n \"PENDING_PROFILE\"로 등록 후 User Id Token이 발급되기 때문에 "
    )
    @PostMapping("/profile")
    public ResponseEntity<SuccessResponse> postUserProfile(@AuthenticationPrincipal User user, @RequestBody UserProfileReqDto dto){
        userService.postUserProfile(user, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }


}
