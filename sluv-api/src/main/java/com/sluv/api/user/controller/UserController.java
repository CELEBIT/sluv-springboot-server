package com.sluv.api.user.controller;


import com.sluv.api.closet.dto.response.ClosetResponse;
import com.sluv.api.comment.dto.reponse.CommentSimpleResponse;
import com.sluv.api.common.response.PaginationCountResponse;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.api.user.dto.*;
import com.sluv.api.user.service.UserService;
import com.sluv.common.annotation.CurrentUserId;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.user.dto.UserSearchInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/user")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "*유저의 프로필 설정 및 수정",
            description = "최초 회원 가입 시 유저의 프로필 설정 및 추후 유저의 프로필 수정" +
                    "\n User의 Status가 \"PENDING_PROFILE\" 일 경우 설정 후 Status를 \"PENDING_CELEB\"로 변경" +
                    "\n User의 Status가 \"ACTIVE\" 일 경우 프로필 정보만 수정" +
                    "\n (User Id Token 필요)" +
                    "\n \"PENDING_PROFILE\"로 등록 후 User Id Token이 발급되기 때문에 "
    )
    @PostMapping("/profile")
    public ResponseEntity<SuccessResponse> postUserProfile(@CurrentUserId Long userId,
                                                           @RequestBody UserProfileReqDto dto) {
        userService.postUserProfile(userId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "특정 유저의 마이페이지 조회", description = "User 토큰 필요")
    @GetMapping("/{userId}/mypage")
    public ResponseEntity<SuccessDataResponse<UserMypageResDto>> getTargetUserMypage(@CurrentUserId Long userId,
                                                                                     @PathVariable("userId") Long targetId) {
        UserMypageResDto response = userService.getUserMypage(userId, targetId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "현재 유저의 마이페이지 조회", description = "User 토큰 필요")
    @GetMapping("/mypage")
    public ResponseEntity<SuccessDataResponse<UserMypageResDto>> getUserMypage(@CurrentUserId Long userId) {
        UserMypageResDto response = userService.getUserMypage(userId, null);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "특정 유저의 아이템 목록 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/{userId}/item")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<ItemSimpleDto>>> getUserItem(
            @CurrentUserId Long userId, @PathVariable("userId") Long targetId, Pageable pageable) {
        PaginationResponse<ItemSimpleDto> response = userService.getUserItem(userId, targetId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*특정 유저의 옷장 목록 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/{userId}/closet")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<ClosetResponse>>> getUserCloset(
            @CurrentUserId Long userId, @PathVariable("userId") Long targetId, Pageable pageable) {
        PaginationResponse<ClosetResponse> response = userService.getUserCloset(userId, targetId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*유저의 이메일, 소셜 종류 조회", description = "User 토큰 필요.")
    @GetMapping("/social")
    public ResponseEntity<SuccessDataResponse<UserSocialDto>> getUserSocialData(@CurrentUserId Long userId) {
        UserSocialDto response = userService.getUserSocialData(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*유저의 프로필 이미지 수정", description = "User 토큰 필요. Pagination 적용.")
    @PatchMapping("/profileImg")
    public ResponseEntity<SuccessResponse> patchUserProfileImg(@CurrentUserId Long userId,
                                                               @RequestBody UserProfileImgReqDto dto) {

        userService.patchUserProfileImg(userId, dto);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*유저의 프로필 이미지 삭제", description = "null로 변경. User 토큰 필요.")
    @DeleteMapping("/profileImg")
    public ResponseEntity<SuccessResponse> deleteUserProfileImg(@CurrentUserId Long userId) {

        userService.deleteUserProfileImg(userId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*유저가 작성한 Item 게시글 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/item")
    public ResponseEntity<SuccessDataResponse<PaginationCountResponse<ItemSimpleDto>>> getUserUploadItem(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationCountResponse<ItemSimpleDto> response = userService.getUserUploadItem(userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*유저가 작성한 Question 게시글 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/question")
    public ResponseEntity<SuccessDataResponse<PaginationCountResponse<QuestionSimpleResDto>>> getUserUploadQuestion(
            @CurrentUserId Long userId, Pageable pageable) {

        PaginationCountResponse<QuestionSimpleResDto> response = userService.getUserUploadQuestion(userId,
                pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*유저가 작성한 Comment 게시글 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/comment")
    public ResponseEntity<SuccessDataResponse<PaginationCountResponse<CommentSimpleResponse>>> getUserUploadComment(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationCountResponse<CommentSimpleResponse> response = userService.getUserUploadComment(userId,
                pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    /**
     * 1. 팔로워수 2. 아이템 업로드 수 3. item Like 받은 수 4. question Like 받은 수 5. comment Like 받은 수
     */
    @Operation(summary = "*인기 스러버 조회", description = "User 토큰 필요. 정적으로 10개 검색")
    @GetMapping("/hotSluver")
    public ResponseEntity<SuccessDataResponse<List<UserSearchInfoDto>>> getHotSluver(@CurrentUserId Long userId,
                                                                                     @Nullable @RequestParam("celebId") Long celebId) {
        List<UserSearchInfoDto> response = userService.getHotSluver(userId, celebId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*약관 동의", description = "광고성 정보 수신 및 마케팅 활용 동의")
    @PostMapping("/terms")
    public ResponseEntity<SuccessDataResponse<UserTermsResDto>> postTerms(@CurrentUserId Long userId) {
        UserTermsResDto response = userService.postTerms(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*약관 동의 상태 조회", description = "광고성 정보 수신 및 마케팅 활용 동의 상태 조회")
    @GetMapping("/terms")
    public ResponseEntity<SuccessDataResponse<UserTermsResDto>> getUserTermsStatus(@CurrentUserId Long userId) {
        UserTermsResDto response = userService.findUserTermsStatus(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*회원 탈퇴", description = "회원 탈퇴 기능")
    @PostMapping("/withdraw")
    public ResponseEntity<SuccessResponse> withdrawUser(@CurrentUserId Long userId,
                                                        @RequestBody UserWithdrawReqDto dto) {
        userService.withdrawUser(userId, dto);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "사용자 알람 수신 여부 수정", description = "기본값 false, 호출 시 현재 상태의 반대로 수정")
    @PatchMapping("/alarm-status")
    public ResponseEntity<SuccessDataResponse<UserAlarmStatusResponse>> changeUserAlarmStatus(@CurrentUserId Long userId) {
        UserAlarmStatusResponse response = userService.changeAlarmStatus(userId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

}
