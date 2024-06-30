package com.sluv.server.domain.alarm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmMessage {
    ITEM_LIKE("%s 님이 회원님이 올린 아이템 정보를 좋아해요"),
    ITEM_EDIT("회원님이 올린 아이템 정보에 수정 요청이 들어왔어요"),
    QUESTION_LIKE("%s 님이 회원님이 올린 게시글을 좋아해요"),
    QUESTION_COMMENT("%s 님이 회원님이 올린 게시글에 댓글을 남겼어요"),
    COMMENT_LIKE("%s 님이 회원님이 올린 댓글을 좋아해요"),
    COMMENT_SUB("%s 님이 회원님이 올린 댓글에 답글을 남겼어요"),
    COMMENT_REPORT_BY_AI("회원님이 올린 댓글이 부적절하여 클린봇에 의해 삭제되었어요"),
    USER_FOLLOW("%s 님이 회원님을 팔로우하기 시작했어요");

    private final String message;

    public static String getMessageWithUserName(String userNickname, AlarmMessage message) {
        return String.format(message.getMessage(), userNickname);
    }
}
