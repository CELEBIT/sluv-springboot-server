package com.sluv.server.domain.user.controller;

import com.sluv.server.domain.celeb.dto.InterestedCelebCategoryResDto;
import com.sluv.server.domain.celeb.dto.InterestedCelebParentResDto;
import com.sluv.server.domain.celeb.dto.InterestedCelebPostReqDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.service.UserService;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/app/user")
@RequiredArgsConstructor
public class UserCelebController {
    private final UserService userService;

    @Operation(
            summary = "*현재 유저의 관심 샐럽을 카테고리를 기준으로 조회",
            description = "현재 유저를 기준으로 InterstedCeleb 테이블에서 일치하는 Celeb을 카테고리를 기준으로 검색"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/celeb/category")
    public ResponseEntity<SuccessDataResponse<List<InterestedCelebCategoryResDto>>> getInterestedCelebByCategory(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok().body(SuccessDataResponse.<List<InterestedCelebCategoryResDto>>builder()
                .result(userService.getInterestedCelebByCategory(user))
                .build());
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
    public ResponseEntity<SuccessResponse> postInterestedCeleb(@AuthenticationPrincipal User user,
                                                               @RequestBody InterestedCelebPostReqDto dto) {
        userService.postInterestedCeleb(user, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*현재 유저의 관심 샐럽을 등록순을 기준으로 조회",
            description = "현재 유저를 기준으로 InterstedCeleb 테이블에서 일치하는 Celeb을 등록순을 기준으로 검색"
    )
    @GetMapping("/celeb")
    public ResponseEntity<SuccessDataResponse<List<InterestedCelebParentResDto>>> getInterestedCelebByPostTime(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok().body(SuccessDataResponse.<List<InterestedCelebParentResDto>>builder()
                .result(userService.getInterestedCelebByPostTime(user))
                .build());
    }

    @Operation(
            summary = "특정 유저의 관심 샐럽을 등록순을 기준으로 조회",
            description = "특정 유저를 기준으로 InterstedCeleb 테이블에서 일치하는 Celeb을 등록순을 기준으로 검색"
    )
    @GetMapping("/{userId}/celeb")
    public ResponseEntity<SuccessDataResponse<List<InterestedCelebParentResDto>>> getTargetUserInterestedCelebByPostTime(
            @PathVariable("userId") Long userId) {

        return ResponseEntity.ok().body(SuccessDataResponse.<List<InterestedCelebParentResDto>>builder()
                .result(userService.getTargetUserInterestedCelebByPostTime(userId))
                .build());
    }

    @Operation(
            summary = "*특정 유저의 관심 샐럽을 카테고리를 기준으로 조회",
            description = "특정 유저를 기준으로 InterstedCeleb 테이블에서 일치하는 Celeb을 카테고리를 기준으로 검색"
    )
    @GetMapping("/{userId}/celeb/category")
    public ResponseEntity<SuccessDataResponse<List<InterestedCelebCategoryResDto>>> getTargetUserInterestedCelebByCategory(
            @PathVariable("userId") Long userId) {

        return ResponseEntity.ok().body(SuccessDataResponse.<List<InterestedCelebCategoryResDto>>builder()
                .result(userService.getTargetUserInterestedCelebByCategory(userId))
                .build());
    }

}
