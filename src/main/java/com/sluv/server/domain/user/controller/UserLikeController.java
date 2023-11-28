package com.sluv.server.domain.user.controller;

import com.sluv.server.domain.comment.dto.CommentSimpleResDto;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.service.UserService;
import com.sluv.server.global.common.response.PaginationCountResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/app/user")
@RequiredArgsConstructor
public class UserLikeController {

    private final UserService userService;

    @Operation(
            summary = "유저가 좋아요한 아이템 조회",
            description = """
                    유저가 좋아요한 아이템 조회\n
                    User Id Token 필요
                        -> Id를 기준으로 조회\n
                    Pagination 적용\n
                    """
    )
    @GetMapping("/like/item")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<ItemSimpleResDto>>> getUserLikeItem(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<ItemSimpleResDto>>builder()
                        .result(userService.getUserLikeItem(user, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "유저가 좋아요한 Question 게시글 조회",
            description = """
                    유저가 좋아요한 Question 게시글 조회\n
                    User Id Token 필요
                        -> Id를 기준으로 조회\n
                    Pagination 적용\n
                    """
    )
    @GetMapping("/like/question")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<QuestionSimpleResDto>>> getUserLikeQuestion(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<QuestionSimpleResDto>>builder()
                        .result(userService.getUserLikeQuestion(user, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "유저가 좋아요한 Comment 조회",
            description = """
                    유저가 좋아요한 Comment 조회\n
                    User Id Token 필요\n
                        -> Id를 기준으로 조회\n
                    Pagination 적용\n
                    """
    )
    @GetMapping("/like/comment")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<CommentSimpleResDto>>> getUserLikeComment(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<CommentSimpleResDto>>builder()
                        .result(userService.getUserLikeComment(user, pageable))
                        .build()
        );
    }
}
