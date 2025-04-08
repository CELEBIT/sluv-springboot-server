package com.sluv.api.user.service;

import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserBlock;
import com.sluv.domain.user.service.UserBlockDomainService;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserBlockService {

    private final UserDomainService userDomainService;
    private final UserBlockDomainService userBlockDomainService;

    @Transactional
    public void postUserBlock(Long userId, Long targetId) {
        User user = userDomainService.findById(userId);
        User targetUser = userDomainService.findById(targetId);
        boolean blockStatus = userBlockDomainService.getBlockStatus(user, targetUser);

        if (blockStatus) {
            userBlockDomainService.deleteUserBlock(user, targetUser);
        } else {
            userBlockDomainService.postUserBlock(UserBlock.toEntity(user, targetUser));
        }

    }
}
