package com.sluv.server.domain.alarm.service;

import com.sluv.server.domain.alarm.enums.AlarmMessage;
import com.sluv.server.domain.alarm.enums.AlarmType;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.firebase.FcmNotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAlarmService {
    private final FcmNotificationService fcmNotificationService;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final ItemRepository itemRepository;

    private static final String ALARM_TITLE = "[스럽]";

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutFollow(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(targetUserId).orElseThrow(UserNotFoundException::new);

        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.USER_FOLLOW);
        fcmNotificationService.sendFCMNotification(
                targetUser.getId(), ALARM_TITLE, message, AlarmType.USER, user.getId()
        );
    }

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutFollowItem(Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        List<Long> followerIds = followRepository.getAllFollower(userId)
                .stream()
                .map(follow -> follow.getFollower().getId())
                .toList();

        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.USER_FOLLOW_ITEM);
        fcmNotificationService.sendFCMNotificationMulticast(
                followerIds, ALARM_TITLE, message, AlarmType.ITEM, item.getId()
        );
    }

}
