package com.sluv.domain.alarm.repository.impl;

import com.sluv.domain.alarm.entity.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlarmRepositoryCustom {
    Page<Alarm> findAllByUserId(Long userId, Pageable pageable);

    Boolean checkAllRead(Long userId);
}
