package com.sluv.server.domain.user.controller;

import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.service.UserRecentService;
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
    private final UserRecentService userRecentService;

    @Operation(summary = "유저의 최근 본 아이템 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/recent/item")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<ItemSimpleResDto>>> getUserRecentItem(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<ItemSimpleResDto>>builder()
                        .result(userRecentService.getUserRecentItem(user, pageable))
                        .build()
        );
    }

    @Operation(summary = "유저의 최근 본 Question 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/recent/question")
    public ResponseEntity<SuccessDataResponse<PaginationCountResDto<QuestionSimpleResDto>>> getUserRecentQuestion(
            @AuthenticationPrincipal User user, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationCountResDto<QuestionSimpleResDto>>builder()
                        .result(userRecentService.getUserRecentQuestion(user, pageable))
                        .build()
        );
    }
}
