package com.sluv.api.alarm.service;

import com.sluv.domain.alarm.dto.AlarmElement;
import com.sluv.domain.alarm.enums.AlarmMessage;
import com.sluv.domain.alarm.enums.AlarmType;
import com.sluv.domain.alarm.service.AlarmDomainService;
import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.service.CommentDomainService;
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
public class CommentAlarmService {

    private final AlarmDomainService alarmDomainService;
    private final UserDomainService userDomainService;
    private final CommentDomainService commentDomainService;

    private final FcmNotificationService fcmNotificationService;

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutCommentLike(Long userId, Long commentId) {
        User user = userDomainService.findById(userId);
        Comment comment = commentDomainService.findById(commentId);
        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.COMMENT_LIKE);
        sendMessageTypeComment(comment.getUser().getId(), comment, message, user);
    }

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutComment(Long userId, Long commentId, User sender) {
        User user = userDomainService.findById(userId);
        Comment comment = commentDomainService.findById(commentId);
        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.QUESTION_COMMENT);
        sendMessageTypeComment(comment.getQuestion().getUser().getId(), comment, message, sender);
    }

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutSubComment(Long userId, Long commentId, User sender) {
        User user = userDomainService.findById(userId);
        Comment comment = commentDomainService.findById(commentId);
        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.COMMENT_SUB);
        sendMessageTypeComment(comment.getUser().getId(), comment, message, sender);
    }

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutReportByAI(Long commentId, User sender) {
        Comment comment = commentDomainService.findById(commentId);
        String message = AlarmMessage.COMMENT_REPORT_BY_AI.getMessage();
        sendMessageTypeComment(comment.getUser().getId(), comment, message, sender);
    }

    private void sendMessageTypeComment(Long receiverId, Comment comment, String message, User sender) {
        AlarmElement alarmElement = AlarmElement.of(null, comment.getQuestion(), comment, sender);
        alarmDomainService.saveAlarm(comment.getUser(), ALARM_TITLE, message, AlarmType.COMMENT, alarmElement);
        fcmNotificationService.sendFCMNotification(
                receiverId, ALARM_TITLE, message, AlarmType.COMMENT, getIdsAboutComment(comment)
        );
    }

    private HashMap<String, Long> getIdsAboutComment(Comment comment) {
        HashMap<String, Long> ids = new HashMap<>();
        ids.put("questionId", comment.getQuestion().getId());
        ids.put("commentId", comment.getId());
        return ids;
    }

}
