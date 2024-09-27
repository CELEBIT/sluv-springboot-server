package com.sluv.domain.alarm.repository;

import com.sluv.domain.alarm.entity.Alarm;
import com.sluv.domain.alarm.repository.impl.AlarmRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryCustom {
    void deleteAllByUserId(Long userId);
}
