package com.sluv.server.domain.alarm.service;

import com.sluv.server.domain.alarm.dto.AlarmElement;
import com.sluv.server.domain.alarm.enums.AlarmMessage;
import com.sluv.server.domain.alarm.enums.AlarmType;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.user.entity.Follow;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.firebase.FcmNotificationService;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemAlarmService {
    private final FcmNotificationService fcmNotificationService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final FollowRepository followRepository;

    private final AlarmService alarmService;

    private static final String ALARM_TITLE = "[스럽]";

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutItemLike(Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.ITEM_LIKE);
        sendMessageTypeItem(user, item, message);
    }

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutItemEdit(Long itemId, Long itemEditReqId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        String message = AlarmMessage.ITEM_EDIT.getMessage();
        sendMessageTypeItemEdit(itemEditReqId, item, message);
    }

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutFollowItem(Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        List<User> follower = followRepository.getAllFollower(userId).stream().map(Follow::getFollower)
                .toList();

        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.USER_FOLLOW_ITEM);
        sendMulticastMessageTypeItem(follower, item, message);
    }

    private void sendMessageTypeItem(User user, Item item, String message) {
        AlarmElement alarmElement = AlarmElement.of(item, null, null, null);
        alarmService.saveAlarm(user, ALARM_TITLE, message, AlarmType.ITEM, alarmElement);
        fcmNotificationService.sendFCMNotification(
                item.getUser().getId(), ALARM_TITLE, message, AlarmType.ITEM, getIdAboutItem(item.getId())
        );
    }

    private void sendMulticastMessageTypeItem(List<User> follower, Item item, String message) {

        List<Long> followerIds = follower
                .stream()
                .map(User::getId)
                .toList();

        AlarmElement alarmElement = AlarmElement.of(item, null, null, null);
        alarmService.saveAllAlarm(follower, ALARM_TITLE, message, AlarmType.ITEM, alarmElement);
        fcmNotificationService.sendFCMNotificationMulticast(
                followerIds, ALARM_TITLE, message, AlarmType.ITEM, getIdAboutItem(item.getId())
        );
    }

    private void sendMessageTypeItemEdit(Long itemEditReqId, Item item, String message) {
        AlarmElement alarmElement = AlarmElement.of(item, null, null, null);
        alarmService.saveAlarm(item.getUser(), ALARM_TITLE, message, AlarmType.EDIT, alarmElement);
        fcmNotificationService.sendFCMNotification(
                item.getUser().getId(), ALARM_TITLE, message, AlarmType.EDIT, getIdAboutItemEdit(itemEditReqId)
        );
    }

    private HashMap<String, Long> getIdAboutItem(Long itemId) {
        HashMap<String, Long> ids = new HashMap<>();
        ids.put("itemId", itemId);
        return ids;
    }

    private HashMap<String, Long> getIdAboutItemEdit(Long itemEditId) {
        HashMap<String, Long> ids = new HashMap<>();
        ids.put("itemEditId", itemEditId);
        return ids;
    }

}
