package com.sluv.infra.alarm.service;

import com.sluv.domain.alarm.dto.AlarmElement;
import com.sluv.domain.alarm.enums.AlarmMessage;
import com.sluv.domain.alarm.enums.AlarmType;
import com.sluv.domain.alarm.service.AlarmDomainService;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.service.QuestionDomainService;
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
public class QuestionAlarmService {

    private final AlarmDomainService alarmDomainService;
    private final UserDomainService userDomainService;
    private final QuestionDomainService questionDomainService;

    private final FcmNotificationService fcmNotificationService;

    @Transactional
    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutQuestionLike(Long senderId, Long questionId) {
        Question question = questionDomainService.findById(questionId);
        if (!senderId.equals(question.getUser().getId())) {
            User sender = userDomainService.findById(senderId);
            String message = AlarmMessage.getMessageWithUserName(sender.getNickname(), AlarmMessage.QUESTION_LIKE);
            sendMessageTypeQuestion(question.getUser(), question, message, sender, AlarmType.QUESTION);
        }
    }

    private void sendMessageTypeQuestion(User receiver, Question question, String message, User sender, AlarmType type) {
        AlarmElement alarmElement = AlarmElement.of(null, question, null, sender);
        alarmDomainService.saveAlarm(receiver, ALARM_TITLE, message, type, alarmElement);
        fcmNotificationService.sendFCMNotification(
                receiver.getId(), ALARM_TITLE, message, type,
                getIdAboutQuestion(question.getId())
        );
    }

    private HashMap<String, Long> getIdAboutQuestion(Long questionId) {
        HashMap<String, Long> ids = new HashMap<>();
        ids.put("communityId", questionId);
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
