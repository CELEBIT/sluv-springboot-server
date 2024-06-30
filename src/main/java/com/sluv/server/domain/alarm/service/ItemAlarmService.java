package com.sluv.server.domain.alarm.service;

import com.sluv.server.domain.alarm.enums.AlarmMessage;
import com.sluv.server.domain.alarm.enums.AlarmType;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.firebase.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemAlarmService {
    private final FcmNotificationService fcmNotificationService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private static final String ALARM_TITLE = "[스럽]";

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutItemLike(Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.ITEM_LIKE);
        fcmNotificationService.sendFCMNotification(
                item.getUser().getId(), ALARM_TITLE, message, AlarmType.ITEM, itemId
        );
    }

    @Async("alarmThreadPoolExecutor")
    public void sendAlarmAboutItemEdit(Long itemId, Long itemEditReqId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        String message = AlarmMessage.ITEM_EDIT.getMessage();
        fcmNotificationService.sendFCMNotification(
                item.getUser().getId(), ALARM_TITLE, message, AlarmType.EDIT, itemEditReqId
        );
    }

}