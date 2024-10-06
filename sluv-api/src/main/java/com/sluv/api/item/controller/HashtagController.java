package com.sluv.api.item.controller;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.item.dto.HashtagPostResponseDto;
import com.sluv.api.item.dto.HashtagRequestDto;
import com.sluv.api.item.dto.HashtagResponseDto;
import com.sluv.api.item.service.HashtagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item/hashtag")
public class HashtagController {

    private final HashtagService hashtagService;

    @Operation(summary = "해쉬태그 검색(Pagination)", description = "Pagination 적용")
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<HashtagResponseDto>>> getHashtag(
            @Nullable @RequestParam String name, Pageable pageable) {
        PaginationResponse<HashtagResponseDto> response = hashtagService.getHashtag(name, pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(summary = "새로운 해쉬태그 등록",
            description = "동시 등록을 방지하기 위해 Hashtag DB 탐색 후, 있으면 해당 값 반환, 없으면 등록 후 반환.")
    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<HashtagPostResponseDto>> postHashtag(
            @RequestBody HashtagRequestDto requestDto) {
        HashtagPostResponseDto response = hashtagService.postHashtag(requestDto);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }
}
