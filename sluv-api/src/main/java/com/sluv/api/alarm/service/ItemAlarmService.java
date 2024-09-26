package com.sluv.api.alarm.service;

import com.sluv.domain.alarm.dto.AlarmElement;
import com.sluv.domain.alarm.enums.AlarmMessage;
import com.sluv.domain.alarm.enums.AlarmType;
import com.sluv.domain.alarm.service.AlarmDomainService;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.user.entity.Follow;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.FollowDomainService;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.firebase.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static com.sluv.common.constant.ConstantData.ALARM_TITLE;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemAlarmService {

    private final AlarmDomainService alarmDomainService;
    private final UserDomainService userDomainService;
    private final ItemDomainService itemDomainService;
    private final FollowDomainService followDomainService;

    private final FcmNotificationService fcmNotificationService;

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutItemLike(Long userId, Long itemId) {
        User user = userDomainService.findById(userId);
        Item item = itemDomainService.findById(itemId);
        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.ITEM_LIKE);
        sendMessageTypeItem(user, item, message, user);
    }

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutItemEdit(Long itemId, Long itemEditReqId, User sender) {
        Item item = itemDomainService.findById(itemId);
        String message = AlarmMessage.ITEM_EDIT.getMessage();
        sendMessageTypeItemEdit(itemEditReqId, item, message, sender);
    }

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutFollowItem(Long userId, Long itemId) {
        User user = userDomainService.findById(userId);
        Item item = itemDomainService.findById(itemId);
        List<User> follower = followDomainService.getAllFollower(userId)
                .stream()
                .map(Follow::getFollower)
                .toList();

        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.USER_FOLLOW_ITEM);
        sendMulticastMessageTypeItem(follower, item, message, user);
    }

    private void sendMessageTypeItem(User user, Item item, String message, User sender) {
        AlarmElement alarmElement = AlarmElement.of(item, null, null, sender);
        alarmDomainService.saveAlarm(user, ALARM_TITLE, message, AlarmType.ITEM, alarmElement);
        fcmNotificationService.sendFCMNotification(
                item.getUser().getId(), ALARM_TITLE, message, AlarmType.ITEM, getIdAboutItem(item.getId())
        );
    }

    private void sendMulticastMessageTypeItem(List<User> follower, Item item, String message, User sender) {

        List<Long> followerIds = follower
                .stream()
                .map(User::getId)
                .toList();

        AlarmElement alarmElement = AlarmElement.of(item, null, null, sender);
        alarmDomainService.saveAllAlarm(follower, ALARM_TITLE, message, AlarmType.ITEM, alarmElement);
        fcmNotificationService.sendFCMNotificationMulticast(
                followerIds, ALARM_TITLE, message, AlarmType.ITEM, getIdAboutItem(item.getId())
        );
    }

    private void sendMessageTypeItemEdit(Long itemEditReqId, Item item, String message, User sender) {
        AlarmElement alarmElement = AlarmElement.of(item, null, null, sender);
        alarmDomainService.saveAlarm(item.getUser(), ALARM_TITLE, message, AlarmType.EDIT, alarmElement);
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
