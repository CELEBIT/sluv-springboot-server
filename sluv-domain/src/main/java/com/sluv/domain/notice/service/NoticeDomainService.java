package com.sluv.domain.notice.service;

import com.sluv.domain.notice.entity.Notice;
import com.sluv.domain.notice.exception.NoticeNotFoundException;
import com.sluv.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeDomainService {

    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public Page<Notice> getAllNotice(Pageable pageable) {
        return noticeRepository.getAllNotice(pageable);
    }

    @Transactional(readOnly = true)
    public Notice findById(Long noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(NoticeNotFoundException::new);
    }

}
