package com.sluv.server.domain.comment.controller;

import com.sluv.server.domain.comment.dto.CommentPostReqDto;
import com.sluv.server.domain.comment.dto.CommentReportPostReqDto;
import com.sluv.server.domain.comment.service.CommentService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/comment")
public class CommentController {
    private final CommentService commentService;

    @Operation(
            summary = "*댓글 작성",
            description = "Question 게시글에 댓글 작성하기" +
                    "\n User Id Token 필요"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> postComment(@AuthenticationPrincipal User user, @PathVariable("questionId") Long questionId, @RequestBody CommentPostReqDto dto){
        commentService.postComment(user, questionId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*대댓글 작성",
            description = "Question 게시글에 대댓글 작성" +
                    "\n User Id Token 필요"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{questionId}/{commentId}")
    public ResponseEntity<SuccessResponse> postNestedComment(@AuthenticationPrincipal User user,
                                                       @PathVariable("questionId") Long questionId,
                                                       @PathVariable("commentId") Long commentId,
                                                       @RequestBody CommentPostReqDto dto){
        commentService.postNestedComment(user, questionId, commentId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*댓글 수정",
            description = "Question 게시글에 대댓글 작성" +
                    "\n User Id Token 필요"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> putComment(@AuthenticationPrincipal User user,
                                                             @PathVariable("commentId") Long commentId,
                                                             @RequestBody CommentPostReqDto dto){
        commentService.putComment(user, commentId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*댓글 좋아요",
            description = "댓글의 좋아요 기능" +
                    "\n User Id Token 필요" +
                    "\n 없다면 -> 생성, 있다면 -> 삭제"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{commentId}/like")
    public ResponseEntity<SuccessResponse> postCommentLike(@AuthenticationPrincipal User user,
                                                      @PathVariable("commentId") Long commentId ){
        commentService.postCommentLike(user, commentId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*댓글 신고",
            description = "댓글 신고하기 기능" +
                    "\n User Id Token 필요" +
                    "\n 한 유저가 하나의 댓글 중복 신고 불가."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{commentId}/report")
    public ResponseEntity<SuccessResponse> postCommentReport(@AuthenticationPrincipal User user,
                                                      @PathVariable("commentId") Long commentId,
                                                      @RequestBody CommentReportPostReqDto dto){
        commentService.postCommentReport(user, commentId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

    @Operation(
            summary = "*댓글 삭제",
            description = "댓글 삭제 기능" +
                    "\n 삭제 시 Comment Status를 변경하는 것이 아닌 DB에서 삭제." +
                    "\n User Id Token 필요" +
                    "\n -> 댓글 작성자가 맞는지 확인하기 위해 필요"
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> deleteComment(@AuthenticationPrincipal User user,
                                                             @PathVariable("commentId") Long commentId ){
        commentService.deleteComment(user, commentId);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
