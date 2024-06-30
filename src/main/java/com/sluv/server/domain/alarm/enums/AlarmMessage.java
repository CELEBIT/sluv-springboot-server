package com.sluv.server.domain.alarm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmMessage {
    ITEM_LIKE("%s 님이 회원님이 올린 아이템 정보를 좋아해요"),
    ITEM_EDIT("회원님이 올린 아이템 정보에 수정 요청이 들어왔어요"),
    QUESTION_LIKE("%s 님이 회원님이 올린 게시글을 좋아해요");

    private final String message;

    public static String getMessageWithUserName(String userNickname, AlarmMessage message) {
        return String.format(message.getMessage(), userNickname);
    }
}
