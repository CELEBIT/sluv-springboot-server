package com.sluv.server.domain.alarm.repository;

import com.sluv.server.domain.alarm.entity.Alarm;
import com.sluv.server.domain.alarm.repository.impl.AlarmRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryCustom {
    void deleteAllByUserId(Long userId);
}
