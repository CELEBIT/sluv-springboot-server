package com.sluv.server.global.discord;

import com.sluv.server.domain.user.entity.User;

public interface WebHookService {
    void sendSingupMessage(User user);
}
