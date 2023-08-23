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

    public PaginationResDto<NoticeSimpleResDto> getAllNotice(Pageable pageable) {

        Page<Notice> noticePage = noticeRepository.getAllNotice(pageable);

        List<NoticeSimpleResDto> content = noticePage.stream()
                                                        .map(NoticeSimpleResDto::of)
                                                        .toList();

        return PaginationResDto.<NoticeSimpleResDto>builder()
                .page(noticePage.getNumber())
                .hasNext(noticePage.hasNext())
                .content(content)
                .build();
    }

    public NoticeDetailResDto getNoticeDetail(Long noticeId) {

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(NoticeNotFoundException::new);

        return NoticeDetailResDto.of(notice);
    }
}
