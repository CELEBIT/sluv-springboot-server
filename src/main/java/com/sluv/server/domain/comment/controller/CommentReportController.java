package com.sluv.server.domain.comment.controller;

import com.sluv.server.domain.comment.dto.CommentReportPostReqDto;
import com.sluv.server.domain.comment.service.CommentReportService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<SuccessResponse> postCommentReport(@AuthenticationPrincipal User user,
                                                             @PathVariable("commentId") Long commentId,
                                                             @RequestBody CommentReportPostReqDto dto) {
        commentReportService.postCommentReport(user, commentId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }

}
