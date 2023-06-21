package com.sluv.server.domain.user.controller;


import com.sluv.server.domain.celeb.dto.InterestedCelebParentResDto;
import com.sluv.server.domain.celeb.dto.InterestedCelebPostReqDto;
import com.sluv.server.domain.closet.dto.ClosetResDto;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.user.dto.UserMypageResDto;
import com.sluv.server.domain.user.dto.UserProfileReqDto;
import com.sluv.server.domain.user.dto.UserReportReqDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.service.UserReportService;
import com.sluv.server.domain.user.service.UserService;
import com.sluv.server.global.common.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    @Operation(
            summary = "특정 유저의 마이페이지 조회",
            description = """
                    특정 유저의 마이페이지 조회
                    User Id Token 필요
                     -> 특정 유저가 현재 유저와 일치하는지, 혹은 팔로우 여부를 체크
                    """
    )
    @GetMapping("/{userId}/mypage")
    public ResponseEntity<SuccessDataResponse<UserMypageResDto>> getUserMypage(@AuthenticationPrincipal User user, @PathVariable("userId") Long userId){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<UserMypageResDto>builder()
                        .result(userService.getUserMypage(user, userId))
                        .build()
        );
    }
    @Operation(
            summary = "특정 유저의 아이템 목록 조회",
            description = """
                    특정 유저의 아이템 목록 조회\n
                    User Id Token 필요
                        -> 특정 유저의 아이템 스크랩 여부 판단
                    Pagination 적용\n
                    """
    )
    @GetMapping("/{userId}/item")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getUserItem(@AuthenticationPrincipal User user, @PathVariable("userId") Long userId, Pageable pageable){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(userService.getUserItem(user, userId, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "특정 유저의 옷장 목록 조회",
            description = """
                    특정 유저의 옷장 목록 조회\n
                    User Id Token 필요
                        -> 불일치 시 Public 상태의 옷장만 조회
                    Pagination 적용\n
                    """
    )
    @GetMapping("/{userId}/closet")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ClosetResDto>>> getUserCloset(@AuthenticationPrincipal User user, @PathVariable("userId") Long userId, Pageable pageable){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ClosetResDto>>builder()
                        .result(userService.getUserCloset(user, userId, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "유저의 최근 본 아이템 조회",
            description = """
                    유저의 최근 본 아이템 조회\n
                    User Id Token 필요
                        -> Id를 기준으로 조회\n
                    Pagination 적용\n
                    """
    )
    @GetMapping("/recent/item")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<ItemSimpleResDto>>> getUserRecentItem(@AuthenticationPrincipal User user, Pageable pageable){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<ItemSimpleResDto>>builder()
                        .result(userService.getUserRecentItem(user, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "유저의 최근 본 Question 조회",
            description = """
                    유저의 최근 본 Question 조회\n
                    User Id Token 필요
                        -> Id를 기준으로 조회\n
                    Pagination 적용\n
                    """
    )
    @GetMapping("/recent/question")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<QuestionSimpleResDto>>> getUserRecentQuestion(@AuthenticationPrincipal User user, Pageable pageable){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<QuestionSimpleResDto>>builder()
                        .result(userService.getUserRecentQuestion(user, pageable))
                        .build()
        );
    }
    @Operation(
            summary = "유저가 좋아요한 아이템 조회",
            description = """
                    유저가 좋아요한 아이템 조회\n
                    User Id Token 필요
                        -> Id를 기준으로 조회\n
                    Pagination 적용\n
                    """
    )
    @GetMapping("/like/item")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<ItemSimpleResDto>>> getUserLikeItem(@AuthenticationPrincipal User user, Pageable pageable){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<ItemSimpleResDto>>builder()
                        .result(userService.getUserLikeItem(user, pageable))
                        .build()
        );
    }

}
