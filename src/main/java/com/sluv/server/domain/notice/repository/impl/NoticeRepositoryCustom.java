package com.sluv.server.domain.notice.repository.impl;

import com.sluv.server.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    Page<Notice> getAllNotice(Pageable pageable);
}
