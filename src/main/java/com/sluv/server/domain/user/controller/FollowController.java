package com.sluv.server.domain.user.controller;

import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.service.FollowService;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/app/user")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "*유저 팔로우/팔로잉", description = "User 토큰 필요")
    @PostMapping("/{userId}/follow")
    public ResponseEntity<SuccessResponse> postUserFollow(@AuthenticationPrincipal User user,
                                                          @PathVariable(name = "userId") Long userId) {
        followService.postUserFollow(user, userId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "특정 유저를 등록한 팔로워들 조회", description = "User 토큰 필요. Pagination 적용")
    @GetMapping("/{userId}/follower")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<UserSearchInfoDto>>> getUserFollower(
            @AuthenticationPrincipal User user, @PathVariable("userId") Long userId, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<UserSearchInfoDto>>builder()
                        .result(followService.getUserFollower(user, userId, pageable))
                        .build()
        );
    }

    @Operation(summary = "특정 유저가 등록한 팔로잉 조회", description = "User 토큰 필요. Pagination 적용")
    @GetMapping("/{userId}/following")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<UserSearchInfoDto>>> getUserFollowing(
            @AuthenticationPrincipal User user, @PathVariable("userId") Long userId, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<UserSearchInfoDto>>builder()
                        .result(followService.getUserFollowing(user, userId, pageable))
                        .build()
        );
    }

    @Operation(summary = "*현재 유저를 등록한 팔로워들 조회", description = "User 토큰 필요. Pagination 적용")
    @GetMapping("/follower")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<UserSearchInfoDto>>> getNowUserFollower(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<UserSearchInfoDto>>builder()
                        .result(followService.getUserFollower(user, user.getId(), pageable))
                        .build()
        );
    }

    @Operation(summary = "*현재 유저가 등록한 팔로잉 조회", description = "User 토큰 필요. Pagination 적용")
    @GetMapping("/following")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<UserSearchInfoDto>>> getNowUserFollowing(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<UserSearchInfoDto>>builder()
                        .result(followService.getUserFollowing(user, user.getId(), pageable))
                        .build()
        );
    }

}
