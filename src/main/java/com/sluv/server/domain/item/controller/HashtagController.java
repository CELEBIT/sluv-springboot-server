package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.HashtagResponseDto;
import com.sluv.server.domain.item.service.HashtagService;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/item/hashtag")
public class HashtagController {

    private final HashtagService hashtagService;

    @Operation(
            summary = "해쉬태그 검색(Pagination)",
            description = "Item 등록 시 추가 정보에 해쉬태그 등록 시 사용"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청성공"),
            @ApiResponse(responseCode = "5000", description = "서버내부 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "DB 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<List<HashtagResponseDto>>> getHashtag(@RequestParam String name, Pageable pageable){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<HashtagResponseDto>>builder()
                        .result(hashtagService.getHashtag(name, pageable))
                        .build()

        );
    }
}
