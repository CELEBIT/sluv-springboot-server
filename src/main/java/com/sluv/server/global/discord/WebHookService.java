package com.sluv.server.global.discord;

import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.user.entity.User;

public interface WebHookService {
    void sendSingupMessage(User user);

    void sendWithdrawMessage(User user);

    void sendCreateNewBrandMessage(NewBrand newBrand);

    void sendCreateNewCelebMessage(NewCeleb newCeleb);
}
