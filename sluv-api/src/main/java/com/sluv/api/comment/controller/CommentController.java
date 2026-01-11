package com.sluv.api.comment.controller;

import com.sluv.api.comment.dto.reponse.CommentResponse;
import com.sluv.api.comment.dto.reponse.SubCommentPageResponse;
import com.sluv.api.comment.dto.request.CommentPostRequest;
import com.sluv.api.comment.service.CommentService;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.common.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/comment")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "*Question 게시글의 댓글 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/{questionId}")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<CommentResponse>>> getComment(
            @CurrentUserId Long userId,
            @PathVariable("questionId") Long questionId,
            Pageable pageable) {
        PaginationResponse<CommentResponse> response = commentService.getComment(userId, questionId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "*Question 게시글의 댓글의 대댓글 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/{commentId}/subcomment")
    public ResponseEntity<SuccessDataResponse<SubCommentPageResponse<CommentResponse>>> getSubComment(
            @CurrentUserId Long userId,
            @PathVariable("commentId") Long commentId,
            Pageable pageable) {
        SubCommentPageResponse<CommentResponse> response = commentService.getSubComment(userId, commentId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "*댓글 상세 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/detail/{commentId}")
    public ResponseEntity<SuccessDataResponse<CommentResponse>> getCommentDetail(
            @CurrentUserId Long userId,
            @PathVariable("commentId") Long commentId) {
        CommentResponse response = commentService.getCommentDetail(userId, commentId);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @Operation(summary = "*댓글 작성", description = "User 토큰 필요")
    @PostMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> postComment(@CurrentUserId Long userId,
                                                       @PathVariable("questionId") Long questionId,
                                                       @RequestBody CommentPostRequest request) {
        commentService.postComment(userId, questionId, request);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*대댓글 작성", description = "User 토큰 필요")
    @PostMapping("/{questionId}/{commentId}")
    public ResponseEntity<SuccessResponse> postSubComment(@CurrentUserId Long userId,
                                                          @PathVariable("questionId") Long questionId,
                                                          @PathVariable("commentId") Long commentId,
                                                          @RequestBody CommentPostRequest request) {
        commentService.postSubComment(userId, questionId, commentId, request);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*댓글 수정", description = " User 토큰 필요")
    @PutMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> putComment(@CurrentUserId Long userId,
                                                      @PathVariable("commentId") Long commentId,
                                                      @RequestBody CommentPostRequest request) {
        commentService.putComment(userId, commentId, request);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*댓글 삭제", description = "User 토큰 필요. 삭제 시 Comment Status를 변경하는 것이 아닌 DB에서 삭제.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> deleteComment(@CurrentUserId Long userId,
                                                         @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

    @Operation(summary = "*댓글 좋아요", description = "User 토큰 필요. 없다면 -> 생성, 있다면 -> 삭제")
    @PostMapping("/{commentId}/like")
    public ResponseEntity<SuccessResponse> postCommentLike(@CurrentUserId Long userId,
                                                           @PathVariable("commentId") Long commentId) {
        commentService.postCommentLike(userId, commentId);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }
}
