package com.sluv.server.domain.comment.controller;

import com.sluv.server.domain.comment.dto.CommentPostReqDto;
import com.sluv.server.domain.comment.service.CommentService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> postComment(@AuthenticationPrincipal User user, @PathVariable("questionId") Long questionId, @RequestBody CommentPostReqDto dto){
        commentService.postComment(user, questionId, dto);
        return ResponseEntity.ok().body(
                new SuccessResponse()
        );
    }
}
