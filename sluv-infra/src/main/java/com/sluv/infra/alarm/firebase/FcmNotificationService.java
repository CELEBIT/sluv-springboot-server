package com.sluv.infra.alarm.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Message.Builder;
import com.google.firebase.messaging.Notification;
import com.sluv.domain.alarm.enums.AlarmType;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.exception.UserNotFoundException;
import com.sluv.domain.user.repository.UserRepository;
import com.sluv.infra.alarm.firebase.exception.FcmConnectException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    public void sendFCMNotification(Long userId, String title, String body, AlarmType alarmType,
                                    HashMap<String, Long> ids) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (user.getAlarmStatus()) { // 알람 허용 시
            Message message = getMessage(title, body, user, alarmType, ids);

            try {
                firebaseMessaging.send(message);
            } catch (FirebaseMessagingException e) {
                log.error(e.getMessage());
                throw new FcmConnectException();
            }
        }
    }

    public void sendFCMNotificationMulticast(List<Long> userIds, String title, String body, AlarmType alarmType,
                                             HashMap<String, Long> ids) {
        List<User> users = userRepository.findAllById(userIds);

        for (User user : users) {
            if (user.getAlarmStatus()) { // 알람 허용 시

                Message message = getMessage(title, body, user, alarmType, ids);

                try {
                    firebaseMessaging.send(message);
                } catch (FirebaseMessagingException e) {
                    log.error(e.getMessage());
                    throw new FcmConnectException();
                }
            }
        }
    }

    private Message getMessage(String title, String body, User user, AlarmType alarmType,
                               HashMap<String, Long> ids) {

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Builder messageBuilder = Message.builder()
                .setToken(user.getFcmToken())
                .setNotification(notification)
                .putData("type", alarmType.getName());

        // PutData
        for (String key : ids.keySet()) {
            messageBuilder.putData(key, ids.get(key).toString());
        }

        return messageBuilder.build();

    }

}