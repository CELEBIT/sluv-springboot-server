package com.sluv.api.alarm.service;

import com.sluv.domain.alarm.dto.AlarmElement;
import com.sluv.domain.alarm.enums.AlarmMessage;
import com.sluv.domain.alarm.enums.AlarmType;
import com.sluv.domain.alarm.service.AlarmDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.firebase.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static com.sluv.common.constant.ConstantData.ALARM_TITLE;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAlarmService {

    private final AlarmDomainService alarmDomainService;
    private final UserDomainService userDomainService;

    private final FcmNotificationService fcmNotificationService;

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutFollow(Long userId, Long targetUserId) {
        User user = userDomainService.findById(userId);
        User targetUser = userDomainService.findById(targetUserId);
        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.USER_FOLLOW);
        sendMessageTypeUser(user, targetUser, message);

    }

    private void sendMessageTypeUser(User sender, User targetUser, String message) {
        AlarmElement alarmElement = AlarmElement.of(null, null, null, sender);
        alarmDomainService.saveAlarm(targetUser, ALARM_TITLE, message, AlarmType.QUESTION, alarmElement);
        fcmNotificationService.sendFCMNotification(
                targetUser.getId(), ALARM_TITLE, message, AlarmType.USER, getIdAboutUser(sender.getId())
        );
    }

    private HashMap<String, Long> getIdAboutUser(Long userId) {
        HashMap<String, Long> ids = new HashMap<>();
        ids.put("userId", userId);
        return ids;
    }

}
