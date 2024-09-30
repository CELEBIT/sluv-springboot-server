package com.sluv.infra.discord;

import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserWithdrawReason;

public interface WebHookService {
    void sendSingupMessage(User user);

    void sendWithdrawMessage(User user, UserWithdrawReason reason, String content);

    void sendCreateNewBrandMessage(NewBrand newBrand);

    void sendCreateNewCelebMessage(NewCeleb newCeleb);
}
