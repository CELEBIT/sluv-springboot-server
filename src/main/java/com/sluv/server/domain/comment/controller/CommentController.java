package com.sluv.server.domain.comment.controller;

import com.sluv.server.domain.comment.dto.CommentPostReqDto;
import com.sluv.server.domain.comment.dto.CommentResDto;
import com.sluv.server.domain.comment.dto.SubCommentPageResDto;
import com.sluv.server.domain.comment.service.CommentService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/comment")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "*Question 게시글의 댓글 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/{questionId}")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<CommentResDto>>> getComment(
            @AuthenticationPrincipal User user,
            @PathVariable("questionId") Long questionId,
            Pageable pageable) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<CommentResDto>>builder()
                        .result(commentService.getComment(user, questionId, pageable))
                        .build()
        );
    }

    @Operation(summary = "*Question 게시글의 댓글의 대댓글 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/{commentId}/subcomment")
    public ResponseEntity<SuccessDataResponse<SubCommentPageResDto<CommentResDto>>> getSubComment(
            @AuthenticationPrincipal User user,
            @PathVariable("commentId") Long commentId,
            Pageable pageable) {
        return ResponseEntity.ok().body(
                SuccessDataResponse.<SubCommentPageResDto<CommentResDto>>builder()
                        .result(commentService.getSubComment(user, commentId, pageable))
                        .build()
        );
    }

    @Operation(summary = "*댓글 작성", description = "User 토큰 필요")
    @PostMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> postComment(@AuthenticationPrincipal User user,
                                                       @PathVariable("questionId") Long questionId,
                                                       @RequestBody CommentPostReqDto dto) {
        commentService.postComment(user, questionId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*대댓글 작성", description = "User 토큰 필요")
    @PostMapping("/{questionId}/{commentId}")
    public ResponseEntity<SuccessResponse> postSubComment(@AuthenticationPrincipal User user,
                                                          @PathVariable("questionId") Long questionId,
                                                          @PathVariable("commentId") Long commentId,
                                                          @RequestBody CommentPostReqDto dto) {
        commentService.postSubComment(user, questionId, commentId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*댓글 수정", description = " User 토큰 필요")
    @PutMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> putComment(@AuthenticationPrincipal User user,
                                                      @PathVariable("commentId") Long commentId,
                                                      @RequestBody CommentPostReqDto dto) {
        commentService.putComment(user, commentId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*댓글 삭제", description = "User 토큰 필요. 삭제 시 Comment Status를 변경하는 것이 아닌 DB에서 삭제.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> deleteComment(@AuthenticationPrincipal User user,
                                                         @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(user, commentId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(summary = "*댓글 좋아요", description = "User 토큰 필요. 없다면 -> 생성, 있다면 -> 삭제")
    @PostMapping("/{commentId}/like")
    public ResponseEntity<SuccessResponse> postCommentLike(@AuthenticationPrincipal User user,
                                                           @PathVariable("commentId") Long commentId) {
        commentService.postCommentLike(user, commentId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
