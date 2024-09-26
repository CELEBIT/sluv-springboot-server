package com.sluv.infra.discord;

import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.user.entity.User;

public interface WebHookService {
    void sendSingupMessage(User user);

    void sendWithdrawMessage(User user);

    void sendCreateNewBrandMessage(NewBrand newBrand);

    void sendCreateNewCelebMessage(NewCeleb newCeleb);
}
