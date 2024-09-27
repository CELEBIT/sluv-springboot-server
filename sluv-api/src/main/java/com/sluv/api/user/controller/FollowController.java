package com.sluv.api.user.controller;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.user.service.FollowService;
import com.sluv.common.annotation.CurrentUserId;
import com.sluv.domain.user.dto.UserSearchInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/user")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "*특정 유저를 등록한 팔로워들 조회", description = "User 토큰 필요. Pagination 적용")
    @GetMapping("/{userId}/follower")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<UserSearchInfoDto>>> getUserFollower(
            @CurrentUserId Long userId, @PathVariable("userId") Long targetId, Pageable pageable) {
        PaginationResponse<UserSearchInfoDto> response = followService.getUserFollower(userId, targetId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*특정 유저가 등록한 팔로잉 조회", description = "User 토큰 필요. Pagination 적용")
    @GetMapping("/{userId}/following")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<UserSearchInfoDto>>> getUserFollowing(
            @CurrentUserId Long userId, @PathVariable("userId") Long targetId, Pageable pageable) {
        PaginationResponse<UserSearchInfoDto> response = followService.getUserFollowing(userId, targetId,
                pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*현재 유저를 등록한 팔로워들 조회", description = "User 토큰 필요. Pagination 적용")
    @GetMapping("/follower")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<UserSearchInfoDto>>> getNowUserFollower(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationResponse<UserSearchInfoDto> response = followService.getUserFollower(userId, userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*현재 유저가 등록한 팔로잉 조회", description = "User 토큰 필요. Pagination 적용")
    @GetMapping("/following")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<UserSearchInfoDto>>> getNowUserFollowing(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationResponse<UserSearchInfoDto> response = followService.getUserFollowing(userId, userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*유저 팔로우/팔로잉", description = "User 토큰 필요")
    @PostMapping("/{userId}/follow")
    public ResponseEntity<SuccessResponse> postUserFollow(@CurrentUserId Long userId,
                                                          @PathVariable(name = "userId") Long targetId) {
        followService.postUserFollow(userId, targetId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

}
