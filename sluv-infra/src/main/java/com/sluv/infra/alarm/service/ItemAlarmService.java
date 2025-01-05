package com.sluv.infra.alarm.service;

import com.sluv.domain.alarm.dto.AlarmElement;
import com.sluv.domain.alarm.enums.AlarmMessage;
import com.sluv.domain.alarm.enums.AlarmType;
import com.sluv.domain.alarm.service.AlarmDomainService;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemEditReq;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.ItemEditReqDomainService;
import com.sluv.domain.user.entity.Follow;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.FollowDomainService;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.alarm.firebase.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static com.sluv.common.constant.ConstantData.ALARM_TITLE;

@Service
@RequiredArgsConstructor
public class ItemAlarmService {

    private final AlarmDomainService alarmDomainService;
    private final UserDomainService userDomainService;
    private final ItemDomainService itemDomainService;
    private final FollowDomainService followDomainService;
    private final ItemEditReqDomainService itemEditReqDomainService;

    private final FcmNotificationService fcmNotificationService;

    @Transactional
    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutItemLike(Long senderId, Long itemId) {
        Item item = itemDomainService.findById(itemId);

        if (!senderId.equals(item.getUser().getId())) {
            User sender = userDomainService.findById(senderId);
            String message = AlarmMessage.getMessageWithUserName(sender.getNickname(), AlarmMessage.ITEM_LIKE);
            sendMessageTypeItem(item.getUser(), item, message, sender);
        }
    }

    @Transactional
    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutItemEdit(Long senderId, Long itemId, Long itemEditReqId) {
        Item item = itemDomainService.findById(itemId);

        if (!senderId.equals(item.getUser().getId())) {
            User sender = userDomainService.findById(senderId);
            ItemEditReq itemEditReq = itemEditReqDomainService.findItemEditReqByIdWithItem(itemEditReqId);
            String message = AlarmMessage.ITEM_EDIT.getMessage();
            sendMessageTypeItemEdit(itemEditReq, item, message, sender);
        }
    }

    @Transactional
    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutFollowItem(Long followeeId, Long itemId) {
        User followee = userDomainService.findById(followeeId);
        Item item = itemDomainService.findById(itemId);
        List<User> followers = followDomainService.getAllFollower(followeeId)
                .stream()
                .map(Follow::getFollower)
                .toList();

        String message = AlarmMessage.getMessageWithUserName(followee.getNickname(), AlarmMessage.USER_FOLLOW_ITEM);
        sendMulticastMessageTypeItem(followers, item, message, followee);
    }

    private void sendMessageTypeItem(User receiver, Item item, String message, User sender) {
        AlarmElement alarmElement = AlarmElement.of(item, null, null, sender, null);
        alarmDomainService.saveAlarm(receiver, ALARM_TITLE, message, AlarmType.ITEM, alarmElement);
        fcmNotificationService.sendFCMNotification(
                receiver.getId(), ALARM_TITLE, message, AlarmType.ITEM, getIdAboutItem(item.getId())
        );
    }

    private void sendMulticastMessageTypeItem(List<User> followers, Item item, String message, User followee) {

        List<Long> followerIds = followers
                .stream()
                .map(User::getId)
                .toList();

        AlarmElement alarmElement = AlarmElement.of(item, null, null, followee, null);
        alarmDomainService.saveAllAlarm(followers, ALARM_TITLE, message, AlarmType.ITEM, alarmElement);
        fcmNotificationService.sendFCMNotificationMulticast(
                followerIds, ALARM_TITLE, message, AlarmType.ITEM, getIdAboutItem(item.getId())
        );
    }

    private void sendMessageTypeItemEdit(ItemEditReq itemEditReq, Item item, String message, User sender) {
        AlarmElement alarmElement = AlarmElement.of(item, null, null, sender, itemEditReq);
        alarmDomainService.saveAlarm(item.getUser(), ALARM_TITLE, message, AlarmType.EDIT, alarmElement);
        fcmNotificationService.sendFCMNotification(
                item.getUser().getId(), ALARM_TITLE, message, AlarmType.EDIT, getIdAboutItemEdit(itemEditReq.getId())
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
