package com.sluv.domain.alarm.service;

import com.sluv.domain.alarm.dto.AlarmElement;
import com.sluv.domain.alarm.entity.Alarm;
import com.sluv.domain.alarm.enums.AlarmStatus;
import com.sluv.domain.alarm.enums.AlarmType;
import com.sluv.domain.alarm.exception.AlarmAccessDeniedException;
import com.sluv.domain.alarm.exception.AlarmNotFoundException;
import com.sluv.domain.alarm.repository.AlarmRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlarmDomainService {

    private final AlarmRepository alarmRepository;

    public void saveAlarm(User user, String title, String body, AlarmType alarmType, AlarmElement alarmElement) {
        alarmRepository.save(Alarm.toEntity(user, title, body, alarmType, alarmElement));
    }

    public void saveAllAlarm(List<User> users, String title, String body, AlarmType alarmType,
                             AlarmElement alarmElement) {

        List<Alarm> alarms = new ArrayList<>();
        for (User user : users) {
            alarms.add(Alarm.toEntity(user, title, body, alarmType, alarmElement));
        }
        alarmRepository.saveAll(alarms);
    }

    public void deleteAlarm(Long userId, Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(AlarmNotFoundException::new);
        if (!alarm.getUser().getId().equals(userId)) {
            throw new AlarmAccessDeniedException();
        }
        alarmRepository.deleteById(alarmId);
    }

    public void deleteAllAlarm(Long userId) {
        alarmRepository.deleteAllByUserId(userId);
    }

    public void patchAlarmStatusToRead(Long userId, Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(AlarmNotFoundException::new);
        if (Objects.equals(alarm.getUser().getId(), userId)) {
            alarm.changeStatus(AlarmStatus.READ);
        }
    }

    public Page<Alarm> findAllByUserId(Long userId, Pageable pageable) {
        return alarmRepository.findAllByUserId(userId, pageable);
    }

    public Boolean checkAllRead(Long userId) {
        return alarmRepository.checkAllRead(userId);
    }

}
