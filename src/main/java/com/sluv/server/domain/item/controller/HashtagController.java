package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.HashtagPostResponseDto;
import com.sluv.server.domain.item.dto.HashtagRequestDto;
import com.sluv.server.domain.item.dto.HashtagResponseDto;
import com.sluv.server.domain.item.service.HashtagService;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item/hashtag")
public class HashtagController {

    private final HashtagService hashtagService;

    @Operation(summary = "해쉬태그 검색(Pagination)", description = "Pagination 적용")
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<HashtagResponseDto>>> getHashtag(
            @Nullable @RequestParam String name, Pageable pageable) {

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<HashtagResponseDto>>builder()
                        .result(hashtagService.getHashtag(name, pageable))
                        .build()

        );
    }

    @Operation(summary = "새로운 해쉬태그 등록",
            description = "동시 등록을 방지하기 위해 Hashtag DB 탐색 후, 있으면 해당 값 반환, 없으면 등록 후 반환.")
    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<HashtagPostResponseDto>> postHashtag(
            @RequestBody HashtagRequestDto requestDto) {

        return ResponseEntity.ok().body(SuccessDataResponse.<HashtagPostResponseDto>builder()
                .result(hashtagService.postHashtag(requestDto))
                .build()
        );
    }
}
