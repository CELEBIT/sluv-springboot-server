package com.sluv.server.domain.notice.service;

import com.sluv.server.domain.notice.dto.NoticeDetailResDto;
import com.sluv.server.domain.notice.dto.NoticeSimpleResDto;
import com.sluv.server.domain.notice.entity.Notice;
import com.sluv.server.domain.notice.exception.NoticeNotFoundException;
import com.sluv.server.domain.notice.repository.NoticeRepository;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    /**
     * 공지사항 리스트 조회
     */
    public PaginationResDto<NoticeSimpleResDto> getAllNotice(Pageable pageable) {
        // 공지사항 리스트 Page 조회
        Page<Notice> noticePage = noticeRepository.getAllNotice(pageable);

        // Content가 될 Dto 제작
        List<NoticeSimpleResDto> content = noticePage.stream()
                                                        .map(NoticeSimpleResDto::of)
                                                        .toList();

        return PaginationResDto.<NoticeSimpleResDto>builder()
                .page(noticePage.getNumber())
                .hasNext(noticePage.hasNext())
                .content(content)
                .build();
    }

    /**
     * 공지사항 상세 조회
     */
    public NoticeDetailResDto getNoticeDetail(Long noticeId) {

        // noticeId에 해당하는 공지사항 조회
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(NoticeNotFoundException::new);

        return NoticeDetailResDto.of(notice);
    }
}
