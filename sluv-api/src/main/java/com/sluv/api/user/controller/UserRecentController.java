package com.sluv.api.user.controller;

import com.sluv.api.common.response.PaginationCountResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.user.service.UserRecentService;
import com.sluv.common.annotation.CurrentUserId;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/app/user")
@RequiredArgsConstructor
public class UserRecentController {
    private final UserRecentService userRecentService;

    @Operation(summary = "*유저의 최근 본 아이템 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/recent/item")
    public ResponseEntity<SuccessDataResponse<PaginationCountResponse<ItemSimpleDto>>> getUserRecentItem(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationCountResponse<ItemSimpleDto> response = userRecentService.getUserRecentItem(userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "*유저의 최근 본 Question 조회", description = "User 토큰 필요. Pagination 적용.")
    @GetMapping("/recent/question")
    public ResponseEntity<SuccessDataResponse<PaginationCountResponse<QuestionSimpleResDto>>> getUserRecentQuestion(
            @CurrentUserId Long userId, Pageable pageable) {
        PaginationCountResponse<QuestionSimpleResDto> response = userRecentService.getUserRecentQuestion(
                userId, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }
}
