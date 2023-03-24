package com.sluv.server.domain.item.controller;

import com.sluv.server.domain.item.dto.HashtagResponseDto;
import com.sluv.server.domain.item.service.HashtagService;
import com.sluv.server.global.common.response.SuccessDataResponse;
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

    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<List<HashtagResponseDto>>> getHashtag(@RequestParam String name, Pageable pageable){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<List<HashtagResponseDto>>builder()
                        .result(hashtagService.getHashtag(name, pageable))
                        .build()

        );
    }
}
