package com.sluv.server.global.discord;

import org.springframework.http.HttpStatus;

public class DiscordWebHookException extends WebHookException {
    private static final int ERROR_CODE = 6000;
    private static final String MESSAGE = "Discord WebHook 전송에 실패했습니다.";
    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public DiscordWebHookException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
