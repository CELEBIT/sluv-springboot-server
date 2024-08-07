package com.sluv.server.domain.alarm.service;

import com.sluv.server.domain.alarm.dto.AlarmElement;
import com.sluv.server.domain.alarm.enums.AlarmMessage;
import com.sluv.server.domain.alarm.enums.AlarmType;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.exception.QuestionNotFoundException;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.firebase.FcmNotificationService;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionAlarmService {
    private final FcmNotificationService fcmNotificationService;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    private final AlarmService alarmService;

    private static final String ALARM_TITLE = "[스럽]";

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutQuestionLike(Long userId, Long questionId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.QUESTION_LIKE);
        sendMessageTypeQuestion(user, question, message, user);
    }

    private void sendMessageTypeQuestion(User user, Question question, String message, User sender) {
        AlarmElement alarmElement = AlarmElement.of(null, question, null, sender);
        alarmService.saveAlarm(user, ALARM_TITLE, message, AlarmType.QUESTION, alarmElement);
        fcmNotificationService.sendFCMNotification(
                question.getUser().getId(), ALARM_TITLE, message, AlarmType.QUESTION,
                getIdAboutQuestion(question.getId())
        );
    }

    private HashMap<String, Long> getIdAboutQuestion(Long questionId) {
        HashMap<String, Long> ids = new HashMap<>();
        ids.put("questionId", questionId);
        return ids;
    }

}
