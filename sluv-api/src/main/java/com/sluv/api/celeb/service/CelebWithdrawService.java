package com.sluv.api.celeb.service;

import com.sluv.domain.celeb.repository.InterestedCelebRepository;
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
