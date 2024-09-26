package com.sluv.infra.discord;

import com.sluv.common.exception.HttpStatusCode;

public class DiscordWebHookException extends WebHookException {
    private static final int ERROR_CODE = 6000;
    private static final String MESSAGE = "Discord WebHook 전송에 실패했습니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.INTERNAL_SERVER_ERROR;

    public DiscordWebHookException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
