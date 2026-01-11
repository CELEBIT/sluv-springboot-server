package com.sluv.api.user.controller;

import com.sluv.api.celeb.dto.request.InterestedCelebPostRequest;
import com.sluv.api.celeb.dto.response.InterestedCelebCategoryResponse;
import com.sluv.api.celeb.dto.response.InterestedCelebParentResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.user.service.UserCelebService;
import com.sluv.common.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/user")
@RequiredArgsConstructor
public class UserCelebController {

    private final UserCelebService userCelebService;

    @Operation(summary = "*현재 유저의 관심 샐럽을 카테고리를 기준으로 조회",
            description = "현재 유저를 기준으로 InterstedCeleb 테이블에서 일치하는 Celeb을 카테고리를 기준으로 검색")
    @GetMapping("/celeb/category")
    public ResponseEntity<SuccessDataResponse<List<InterestedCelebCategoryResponse>>> getInterestedCelebByCategory(
            @CurrentUserId Long userId) {
        List<InterestedCelebCategoryResponse> response = userCelebService.getInterestedCelebByCategory(
                userId);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "*현재 유저의 관심 샐럽을 등록순을 기준으로 조회",
            description = "현재 유저를 기준으로 InterstedCeleb 테이블에서 일치하는 Celeb을 등록순을 기준으로 검색")
    @GetMapping("/celeb")
    public ResponseEntity<SuccessDataResponse<List<InterestedCelebParentResponse>>> getInterestedCelebByPostTime(
            @CurrentUserId Long userId) {
        List<InterestedCelebParentResponse> response = userCelebService.getInterestedCelebByPostTime(
                userId);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "특정 유저의 관심 샐럽을 등록순을 기준으로 조회",
            description = "특정 유저를 기준으로 InterstedCeleb 테이블에서 일치하는 Celeb을 등록순을 기준으로 검색")
    @GetMapping("/{userId}/celeb")
    public ResponseEntity<SuccessDataResponse<List<InterestedCelebParentResponse>>> getTargetUserInterestedCelebByPostTime(
            @PathVariable("userId") Long userId) {
        List<InterestedCelebParentResponse> response = userCelebService.getInterestedCelebByPostTime(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "*특정 유저의 관심 샐럽을 카테고리를 기준으로 조회",
            description = "특정 유저를 기준으로 InterstedCeleb 테이블에서 일치하는 Celeb을 카테고리를 기준으로 검색")
    @GetMapping("/{userId}/celeb/category")
    public ResponseEntity<SuccessDataResponse<List<InterestedCelebCategoryResponse>>> getTargetUserInterestedCelebByCategory(
            @PathVariable("userId") Long userId) {
        List<InterestedCelebCategoryResponse> response = userCelebService.getInterestedCelebByCategory(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "*유저의 관심 셀럽 업데이트", description = "User 토큰 필요")
    @PostMapping("/celeb")
    public ResponseEntity<SuccessResponse> postInterestedCeleb(@CurrentUserId Long userId
            , @RequestBody InterestedCelebPostRequest dto) {
        userCelebService.postInterestedCeleb(userId, dto);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }
}
