package com.sluv.server.domain.alarm.service;

import com.sluv.server.domain.alarm.enums.AlarmMessage;
import com.sluv.server.domain.alarm.enums.AlarmType;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.exception.CommentNotFoundException;
import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.firebase.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentAlarmService {
    private final FcmNotificationService fcmNotificationService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private static final String ALARM_TITLE = "[스럽]";

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutCommentLike(Long userId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.COMMENT_LIKE);
        fcmNotificationService.sendFCMNotification(
                comment.getUser().getId(), ALARM_TITLE, message, AlarmType.COMMENT, comment.getId()
        );
    }

}
