package com.sluv.api.notice.controller;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.common.response.SuccessDataResponse;
import com.sluv.api.notice.dto.NoticeDetailResponse;
import com.sluv.api.notice.dto.NoticeSimpleResponse;
import com.sluv.api.notice.service.NoticeService;
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

    @Operation(
            summary = "공지사항 리스트 조회",
            description = """
                    - 공지사항 리스트 조회 API\n
                    - Pagination 적용
                    """
    )
    @GetMapping("")
    public ResponseEntity<SuccessDataResponse<PaginationResponse<NoticeSimpleResponse>>> getAllNotice(
            Pageable pageable) {

        PaginationResponse<NoticeSimpleResponse> response = noticeService.getAllNotice(pageable);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }

    @Operation(
            summary = "공지사항 상세 조회",
            description = """
                    - 공지사항 상세 조회 API\n
                    - 상세 조회할 공지사항의 Id를 PathVariable로 받음.
                    """
    )
    @GetMapping("/{noticeId}")
    public ResponseEntity<SuccessDataResponse<NoticeDetailResponse>> getNoticeDetail(
            @PathVariable("noticeId") Long noticeId) {

        NoticeDetailResponse response = noticeService.getNoticeDetail(noticeId);
        return ResponseEntity.ok().body(SuccessDataResponse.create(response));
    }
}
