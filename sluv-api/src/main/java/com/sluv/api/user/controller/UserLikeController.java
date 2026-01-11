package com.sluv.api.user.controller;

import com.sluv.api.comment.dto.reponse.CommentSimpleResponse;
import com.sluv.api.common.response.PaginationCountResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.user.service.UserLikeService;
import com.sluv.common.annotation.CurrentUserId;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/user")
@RequiredArgsConstructor
public class UserLikeController {

    private final UserLikeService userLikeService;

    @Operation(summary = "유저가 좋아요한 아이템 조회", description = "User 토큰 필요. Pagination 적용")
    @GetMapping("/like/item")
    public ResponseEntity<SuccessDataResponse<PaginationCountResponse<ItemSimpleDto>>> getUserLikeItem(
            @CurrentUserId Long userId, Pageable pageable) {

        PaginationCountResponse<ItemSimpleDto> response = userLikeService.getUserLikeItem(userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "유저가 좋아요한 Question 게시글 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/like/question")
    public ResponseEntity<SuccessDataResponse<PaginationCountResponse<QuestionSimpleResDto>>> getUserLikeQuestion(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationCountResponse<QuestionSimpleResDto> response = userLikeService.getUserLikeQuestion(userId,
                pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "유저가 좋아요한 Comment 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/like/comment")
    public ResponseEntity<SuccessDataResponse<PaginationCountResponse<CommentSimpleResponse>>> getUserLikeComment(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationCountResponse<CommentSimpleResponse> response = userLikeService.getUserLikeComment(userId,
                pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }
}
