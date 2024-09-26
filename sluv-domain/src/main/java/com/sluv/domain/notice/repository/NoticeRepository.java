package com.sluv.domain.notice.repository;

import com.sluv.domain.notice.entity.Notice;
import com.sluv.domain.notice.repository.impl.NoticeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {
}
