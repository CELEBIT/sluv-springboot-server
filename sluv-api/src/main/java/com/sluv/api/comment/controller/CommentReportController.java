package com.sluv.api.comment.controller;

import com.sluv.api.comment.dto.request.CommentReportPostRequest;
import com.sluv.api.comment.service.CommentReportService;
import com.sluv.api.common.response.SuccessResponse;
import com.sluv.common.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/comment")
public class CommentReportController {
    private final CommentReportService commentReportService;

    @Operation(summary = "*댓글 신고", description = "User 토큰 필요. 한 유저가 하나의 댓글 중복 신고 불가.")
    @PostMapping("/{commentId}/report")
    public ResponseEntity<SuccessResponse> postCommentReport(@CurrentUserId Long userId,
                                                             @PathVariable("commentId") Long commentId,
                                                             @RequestBody CommentReportPostRequest request) {
        commentReportService.postCommentReport(userId, commentId, request);
        return ResponseEntity.ok().body(SuccessResponse.create());
    }

}
