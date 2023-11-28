package com.sluv.server.domain.user.controller;

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
public class UserRecentController {
    private final UserService userService;

    @Operation(
            summary = "유저의 최근 본 아이템 조회",
            description = """
                    유저의 최근 본 아이템 조회\n
                    User Id Token 필요
                        -> Id를 기준으로 조회\n
                    Pagination 적용\n
                    """
    )
    @GetMapping("/recent/item")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<ItemSimpleResDto>>> getUserRecentItem(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<ItemSimpleResDto>>builder()
                        .result(userService.getUserRecentItem(user, pageable))
                        .build()
        );
    }

    @Operation(
            summary = "유저의 최근 본 Question 조회",
            description = """
                    유저의 최근 본 Question 조회\n
                    User Id Token 필요
                        -> Id를 기준으로 조회\n
                    Pagination 적용\n
                    """
    )
    @GetMapping("/recent/question")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<QuestionSimpleResDto>>> getUserRecentQuestion(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<QuestionSimpleResDto>>builder()
                        .result(userService.getUserRecentQuestion(user, pageable))
                        .build()
        );
    }
}
