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
public class AlarmService {
    private final FcmNotificationService fcmNotificationService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private static final String ALARM_TITLE = "[스럽]";

    @Async
    public void sendAlarmAboutItemLike(Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.ITEM_LIKE);
        fcmNotificationService.sendFCMNotification(
                item.getUser().getId(), ALARM_TITLE, message, AlarmType.ITEM, itemId
        );
    }

    @Async
    public void sendAlarmAboutItemEdit(Long userId, Long itemId, Long itemEditReqId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.ITEM_EDIT);
        fcmNotificationService.sendFCMNotification(
                item.getUser().getId(), ALARM_TITLE, message, AlarmType.EDIT, itemEditReqId
        );
    }

    @Async
    public void sendAlarmAboutItemReport(Long userId, Long itemId, Long itemEditReqId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        String message = AlarmMessage.getMessageWithUserName(user.getNickname(), AlarmMessage.ITEM_EDIT);
        fcmNotificationService.sendFCMNotification(
                item.getUser().getId(), ALARM_TITLE, message, AlarmType.EDIT, itemEditReqId
        );
    }
}
