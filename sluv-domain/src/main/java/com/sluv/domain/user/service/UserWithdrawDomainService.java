package com.sluv.domain.user.service;

import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserWithdraw;
import com.sluv.domain.user.enums.UserWithdrawReason;
import com.sluv.domain.user.repository.UserWithdrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWithdrawDomainService {

    private final UserWithdrawRepository userWithdrawRepository;

    @Transactional
    public void saveUserWithdraw(User user, UserWithdrawReason reason, String content) {
        UserWithdraw userWithdraw = UserWithdraw.toEntity(user, reason, content);
        userWithdrawRepository.save(userWithdraw);
    }
}