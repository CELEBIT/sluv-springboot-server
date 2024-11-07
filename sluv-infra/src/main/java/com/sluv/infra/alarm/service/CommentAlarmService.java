package com.sluv.infra.alarm.service;

import com.sluv.domain.alarm.dto.AlarmElement;
import com.sluv.domain.alarm.enums.AlarmMessage;
import com.sluv.domain.alarm.enums.AlarmType;
import com.sluv.domain.alarm.service.AlarmDomainService;
import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.service.CommentDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.alarm.firebase.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static com.sluv.common.constant.ConstantData.ALARM_TITLE;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentAlarmService {

    private final AlarmDomainService alarmDomainService;
    private final UserDomainService userDomainService;
    private final CommentDomainService commentDomainService;

    private final FcmNotificationService fcmNotificationService;

    @Transactional
    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutCommentLike(Long senderId, Long commentId) {
        Comment comment = commentDomainService.findById(commentId);

        if (!senderId.equals(comment.getUser().getId())) {
            User sender = userDomainService.findById(senderId);
            String message = AlarmMessage.getMessageWithUserName(sender.getNickname(), AlarmMessage.COMMENT_LIKE);
            sendMessageTypeComment(comment.getUser(), comment, message, sender);
        }
    }

    @Transactional
    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutComment(Long senderId, Long commentId) {
        log.warn("In the FCM Method : {}", commentId);
        Comment comment = commentDomainService.findById(commentId);
        Long questionUserId = comment.getQuestion().getUser().getId();
        Long parentCommentUserId = null;

        if (comment.getParent() != null) {
            parentCommentUserId = comment.getParent().getUser().getId();
        }

        if (!senderId.equals(questionUserId) && !questionUserId.equals(parentCommentUserId)) {
            User sender = userDomainService.findById(senderId);
            String message = AlarmMessage.getMessageWithUserName(sender.getNickname(), AlarmMessage.QUESTION_COMMENT);
            sendMessageTypeComment(comment.getQuestion().getUser(), comment, message, sender);
        }
    }

    @Transactional
    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutSubComment(Long senderId, Long commentId) {
        Comment comment = commentDomainService.findById(commentId);

        if (!senderId.equals(comment.getParent().getUser().getId()) && !senderId.equals(comment.getQuestion().getUser().getId())) {
            User sender = userDomainService.findById(senderId);
            String message = AlarmMessage.getMessageWithUserName(sender.getNickname(), AlarmMessage.COMMENT_SUB);
            sendMessageTypeComment(comment.getParent().getUser(), comment, message, sender);
        }
    }

    @Transactional
    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutReportByAI(Long commentId, User sender) {
        Comment comment = commentDomainService.findById(commentId);
        String message = AlarmMessage.COMMENT_REPORT_BY_AI.getMessage();
        sendMessageTypeComment(comment.getUser(), comment, message, sender);
    }

    private void sendMessageTypeComment(User receiver, Comment comment, String message, User sender) {
        AlarmElement alarmElement = AlarmElement.of(null, comment.getQuestion(), comment, sender);
        alarmDomainService.saveAlarm(receiver, ALARM_TITLE, message, AlarmType.COMMENT, alarmElement);
        fcmNotificationService.sendFCMNotification(
                receiver.getId(), ALARM_TITLE, message, AlarmType.COMMENT, getIdsAboutComment(comment)
        );
    }

    private HashMap<String, Long> getIdsAboutComment(Comment comment) {
        HashMap<String, Long> ids = new HashMap<>();
        ids.put("questionId", comment.getQuestion().getId());
        ids.put("commentId", comment.getId());
        return ids;
    }

}
