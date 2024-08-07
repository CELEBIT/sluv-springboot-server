package com.sluv.server.domain.alarm.service;

import com.sluv.server.domain.alarm.dto.AlarmElement;
import com.sluv.server.domain.alarm.enums.AlarmMessage;
import com.sluv.server.domain.alarm.enums.AlarmType;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.exception.CommentNotFoundException;
import com.sluv.server.domain.comment.repository.CommentRepository;
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
public class CommentAlarmService {
    private final FcmNotificationService fcmNotificationService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final AlarmService alarmService;

    private static final String ALARM_TITLE = "[스럽]";

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutCommentLike(Long userId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.COMMENT_LIKE);
        sendMessageTypeComment(comment.getUser().getId(), comment, message, user);
    }

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutComment(Long userId, Long commentId, User sender) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.QUESTION_COMMENT);
        sendMessageTypeComment(comment.getQuestion().getUser().getId(), comment, message, sender);
    }

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutSubComment(Long userId, Long commentId, User sender) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.COMMENT_SUB);
        sendMessageTypeComment(comment.getUser().getId(), comment, message, sender);
    }

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutReportByAI(Long commentId, User sender) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        String message = AlarmMessage.COMMENT_REPORT_BY_AI.getMessage();
        sendMessageTypeComment(comment.getUser().getId(), comment, message, sender);
    }

    private void sendMessageTypeComment(Long receiverId, Comment comment, String message, User sender) {
        AlarmElement alarmElement = AlarmElement.of(null, comment.getQuestion(), comment, sender);
        alarmService.saveAlarm(comment.getUser(), ALARM_TITLE, message, AlarmType.COMMENT, alarmElement);
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
