package com.sluv.api.notice.service;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.notice.dto.NoticeDetailResponse;
import com.sluv.api.notice.dto.NoticeSimpleResponse;
import com.sluv.domain.notice.entity.Notice;
import com.sluv.domain.notice.service.NoticeDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeDomainService noticeDomainService;

    /**
     * 공지사항 리스트 조회
     */
    @Transactional(readOnly = true)
    public PaginationResponse<NoticeSimpleResponse> getAllNotice(Pageable pageable) {
        // 공지사항 리스트 Page 조회
        Page<Notice> noticePage = noticeDomainService.getAllNotice(pageable);

        // Content가 될 Dto 제작
        List<NoticeSimpleResponse> content = noticePage.stream()
                .map(NoticeSimpleResponse::of)
                .toList();

        return PaginationResponse.of(noticePage, content);
    }

    /**
     * 공지사항 상세 조회
     */
    @Transactional(readOnly = true)
    public NoticeDetailResponse getNoticeDetail(Long noticeId) {

        // noticeId에 해당하는 공지사항 조회
        Notice notice = noticeDomainService.findById(noticeId);

        return NoticeDetailResponse.of(notice);
    }
}
