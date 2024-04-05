package com.sluv.server.domain.user.service;

import com.sluv.server.domain.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowWithdrawService {
    private final FollowRepository followRepository;

    public void withdrawFollowByUserId(Long userId) {
        followRepository.deleteFolloweeByUserId(userId);
        followRepository.deleteFollowerByUserId(userId);
    }
}
