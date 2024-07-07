package com.sluv.server.domain.alarm.service;

import com.sluv.server.domain.alarm.dto.AlarmElement;
import com.sluv.server.domain.alarm.entity.Alarm;
import com.sluv.server.domain.alarm.enums.AlarmType;
import com.sluv.server.domain.alarm.repository.AlarmRepository;
import com.sluv.server.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

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

}
