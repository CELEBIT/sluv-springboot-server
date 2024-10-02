package com.sluv.infra.alarm.service;

import com.sluv.domain.alarm.dto.AlarmElement;
import com.sluv.domain.alarm.enums.AlarmMessage;
import com.sluv.domain.alarm.enums.AlarmType;
import com.sluv.domain.alarm.service.AlarmDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.alarm.firebase.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static com.sluv.common.constant.ConstantData.ALARM_TITLE;

@Service
@RequiredArgsConstructor
public class UserAlarmService {

    private final AlarmDomainService alarmDomainService;
    private final UserDomainService userDomainService;

    private final FcmNotificationService fcmNotificationService;

    @Transactional
    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutFollow(Long senderId, Long followeeId) {
        User followee = userDomainService.findById(followeeId);
        User sender = userDomainService.findById(senderId);
        String message = AlarmMessage.getMessageWithUserName(sender.getNickname(), AlarmMessage.USER_FOLLOW);
        sendMessageTypeUser(followee, sender, message);

    }

    private void sendMessageTypeUser(User receiver, User sender, String message) {
        AlarmElement alarmElement = AlarmElement.of(null, null, null, sender);
        alarmDomainService.saveAlarm(receiver, ALARM_TITLE, message, AlarmType.QUESTION, alarmElement);
        fcmNotificationService.sendFCMNotification(
                receiver.getId(), ALARM_TITLE, message, AlarmType.USER, getIdAboutUser(sender.getId())
        );
    }

    private HashMap<String, Long> getIdAboutUser(Long userId) {
        HashMap<String, Long> ids = new HashMap<>();
        ids.put("userId", userId);
        return ids;
    }

}
