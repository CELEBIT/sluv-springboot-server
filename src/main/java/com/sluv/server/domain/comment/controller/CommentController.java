package com.sluv.server.domain.comment.controller;

import com.sluv.server.domain.comment.dto.CommentPostReqDto;
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
}
