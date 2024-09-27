package com.sluv.api.alarm.service;

import com.sluv.domain.alarm.dto.AlarmElement;
import com.sluv.domain.alarm.enums.AlarmMessage;
import com.sluv.domain.alarm.enums.AlarmType;
import com.sluv.domain.alarm.service.AlarmDomainService;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.service.QuestionDomainService;
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
@RequiredArgsConstructor
public class QuestionAlarmService {

    private final AlarmDomainService alarmDomainService;
    private final UserDomainService userDomainService;
    private final QuestionDomainService questionDomainService;

    private final FcmNotificationService fcmNotificationService;

    @Transactional
    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutQuestionLike(Long userId, Long questionId) {
        User user = userDomainService.findById(userId);
        Question question = questionDomainService.findById(questionId);
        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.QUESTION_LIKE);
        sendMessageTypeQuestion(user, question, message, user, AlarmType.QUESTION);
    }

    private void sendMessageTypeQuestion(User user, Question question, String message, User sender, AlarmType type) {
        AlarmElement alarmElement = AlarmElement.of(null, question, null, sender);
        alarmDomainService.saveAlarm(user, ALARM_TITLE, message, type, alarmElement);
        fcmNotificationService.sendFCMNotification(
                question.getUser().getId(), ALARM_TITLE, message, type,
                getIdAboutQuestion(question.getId())
        );
    }

    private HashMap<String, Long> getIdAboutQuestion(Long questionId) {
        HashMap<String, Long> ids = new HashMap<>();
        ids.put("questionId", questionId);
        return ids;
    }

    @Transactional
    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutQuestionVote(Long questionId) {
        Question question = questionDomainService.findById(questionId);
        String message = AlarmMessage.QUESTION_VOTE.getMessage();
        sendMessageTypeQuestion(question.getUser(), question, message, null, AlarmType.VOTE);
    }

}
