package com.sluv.admin.comment.controller;

import com.sluv.admin.comment.dto.CommentBlockCountResponse;
import com.sluv.admin.comment.service.CommentService;
import com.sluv.admin.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/backoffice/comment/dashBoard")
public class CommentDashBoardController {

    private final CommentService commentService;

    @Operation(
            summary = "대시보드 - Block된 댓글 통계 조회",
            description = "대시보드에서 Block된 댓글 통계를 조회한다."
    )
    @GetMapping("/blockCount")
    public ResponseEntity<SuccessDataResponse<CommentBlockCountResponse>> getCommentBlockCount() {
        CommentBlockCountResponse response = commentService.getCommentBlockCount();
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

}
