package com.sluv.server.global.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.sluv.server.domain.alarm.enums.AlarmType;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.firebase.exception.FcmConnectException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    public void sendFCMNotification(Long userId, String title, String body, AlarmType alarmType, Long id) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        if (user.getFcmToken() != null) { // user.getAlarmStatus()
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(user.getFcmToken())
                    .setNotification(notification)
                    .putData("type", alarmType.getName())
                    .putData("id", id.toString())
                    .build();

            try {
                firebaseMessaging.send(message);
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
                throw new FcmConnectException();
            }
        }
    }

    public void sendFCMNotificationMulticast(List<Long> userIds, String title, String body, AlarmType alarmType,
                                             Long id) {
        List<User> users = userRepository.findAllById(userIds);

        for (User user : users) {
            if (user.getFcmToken() != null) { // user.getAlarmStatus()
                Notification notification = Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build();

                Message message = Message.builder()
                        .setToken(user.getFcmToken())
                        .setNotification(notification)
                        .putData("type", alarmType.getName())
                        .putData("id", id.toString())
                        .build();

                try {
                    firebaseMessaging.send(message);
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                    throw new FcmConnectException();
                }
            }
        }
    }
}