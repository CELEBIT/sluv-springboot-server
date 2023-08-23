package com.sluv.server.domain.notice.controller;

import com.sluv.server.domain.notice.dto.NoticeDetailResDto;
import com.sluv.server.domain.notice.dto.NoticeSimpleResDto;
import com.sluv.server.domain.notice.service.NoticeService;
import com.sluv.server.global.common.response.PaginationResDto;
import com.sluv.server.global.common.response.SuccessDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<PaginationResDto<NoticeSimpleResDto>>> getAllNotice(Pageable pageable){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<PaginationResDto<NoticeSimpleResDto>>builder()
                        .result(noticeService.getAllNotice(pageable))
                        .build()
        );
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<SuccessDataResponse<NoticeDetailResDto>> getNoticeDetail(@PathVariable("noticeId") Long noticeId){

        return ResponseEntity.ok().body(
                SuccessDataResponse.<NoticeDetailResDto>builder()
                        .result(noticeService.getNoticeDetail(noticeId))
                        .build()
        );
    }
}
