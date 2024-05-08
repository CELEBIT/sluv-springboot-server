package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.celeb.repository.InterestedCelebRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CelebWithdrawService {
    private final InterestedCelebRepository interestedCelebRepository;

    public void withdrawInterestedCelebByUserId(Long userId) {
        interestedCelebRepository.deleteAllByUserId(userId);
    }
}
