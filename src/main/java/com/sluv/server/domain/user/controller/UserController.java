package com.sluv.server.domain.user.controller;


import com.sluv.server.domain.closet.dto.ClosetResDto;
import com.sluv.server.domain.comment.dto.CommentSimpleResDto;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.user.dto.UserMypageResDto;
import com.sluv.server.domain.user.dto.UserProfileImgReqDto;
import com.sluv.server.domain.user.dto.UserProfileReqDto;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
import com.sluv.server.domain.user.dto.UserTermsResDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.service.UserService;
import com.sluv.server.global.common.response.PaginationCountResDto;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
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
    public ResponseEntity<SuccessResponse> postUserProfile(@AuthenticationPrincipal User user,
                                                           @RequestBody UserProfileReqDto dto) {
        userService.postUserProfile(user, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "특정 유저의 마이페이지 조회", description = "User 토큰 필요")
    @GetMapping("/{userId}/mypage")
    public ResponseEntity<SuccessDataResponse<UserMypageResDto>> getTargetUserMypage(@AuthenticationPrincipal User user,
                                                                                     @PathVariable("userId") Long userId) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<UserMypageResDto>builder()
                        .result(userService.getUserMypage(user, userId))
                        .build()
        );
    }

    @Operation(summary = "현재 유저의 마이페이지 조회", description = "User 토큰 필요")
    @GetMapping("/mypage")
    public ResponseEntity<SuccessDataResponse<UserMypageResDto>> getUserMypage(@AuthenticationPrincipal User user) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<UserMypageResDto>builder()
                        .result(userService.getUserMypage(user, null))
                        .build()
        );
    }

    @Operation(summary = "특정 유저의 아이템 목록 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/{userId}/item")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ItemSimpleResDto>>> getUserItem(
            @AuthenticationPrincipal User user, @PathVariable("userId") Long userId, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ItemSimpleResDto>>builder()
                        .result(userService.getUserItem(user, userId, pageable))
                        .build()
        );
    }

    @Operation(summary = "*특정 유저의 옷장 목록 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/{userId}/closet")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<ClosetResDto>>> getUserCloset(
            @AuthenticationPrincipal User user, @PathVariable("userId") Long userId, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<ClosetResDto>>builder()
                        .result(userService.getUserCloset(user, userId, pageable))
                        .build()
        );
    }

    @Operation(summary = "*유저의 프로필 이미지 수정", description = "User 토큰 필요. Pagination 적용.")
    @PatchMapping("/profileImg")
    public ResponseEntity<SuccessResponse> patchUserProfileImg(@AuthenticationPrincipal User user,
                                                               @RequestBody UserProfileImgReqDto dto) {

        userService.patchUserProfileImg(user, dto);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*유저의 프로필 이미지 삭제", description = "null로 변경. User 토큰 필요.")
    @DeleteMapping("/profileImg")
    public ResponseEntity<SuccessResponse> deleteUserProfileImg(@AuthenticationPrincipal User user) {

        userService.deleteUserProfileImg(user);

        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*유저가 작성한 Item 게시글 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/item")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<ItemSimpleResDto>>> getUserUploadItem(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<ItemSimpleResDto>>builder()
                        .result(userService.getUserUploadItem(user, pageable))
                        .build()
        );
    }

    @Operation(summary = "*유저가 작성한 Question 게시글 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/question")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<QuestionSimpleResDto>>> getUserUploadQuestion(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<QuestionSimpleResDto>>builder()
                        .result(userService.getUserUploadQuestion(user, pageable))
                        .build()
        );
    }

    @Operation(summary = "*유저가 작성한 Comment 게시글 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/comment")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<CommentSimpleResDto>>> getUserUploadComment(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<CommentSimpleResDto>>builder()
                        .result(userService.getUserUploadComment(user, pageable))
                        .build()
        );
    }

    /**
     * 1. 팔로워수 2. 아이템 업로드 수 3. item Like 받은 수 4. question Like 받은 수 5. comment Like 받은 수
     */
    @Operation(summary = "*인기 스러버 조회", description = "User 토큰 필요. 정적으로 10개 검색")
    @GetMapping("/hotSluver")
    public ResponseEntity<SuccessDataResponse<List<UserSearchInfoDto>>> getHotSluver(@AuthenticationPrincipal User user,
                                                                                     @Nullable @RequestParam("celebId") Long celebId) {
        log.info("이번주 인기 스러버 조회");
        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<UserSearchInfoDto>>builder()
                        .result(userService.getHotSluver(user, celebId))
                        .build()
        );
    }

    @Operation(summary = "*약관 동의", description = "광고성 정보 수신 및 마케팅 활용 동의")
    @PostMapping("/terms")
    public ResponseEntity<SuccessDataResponse<UserTermsResDto>> postTerms(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<UserTermsResDto>builder()
                        .result(userService.postTerms(user))
                        .build()
        );
    }

    @Operation(summary = "*약관 동의 상태 조회", description = "광고성 정보 수신 및 마케팅 활용 동의 상태 조회")
    @GetMapping("/terms")
    public ResponseEntity<SuccessDataResponse<UserTermsResDto>> getUserTermsStatus(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<UserTermsResDto>builder()
                        .result(UserTermsResDto.of(user))
                        .build()
        );
    }
}
