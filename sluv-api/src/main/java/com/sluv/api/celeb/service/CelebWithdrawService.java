package com.sluv.api.celeb.service;

import com.sluv.domain.celeb.repository.InterestedCelebRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CelebWithdrawService {
    private final InterestedCelebRepository interestedCelebRepository;

    @Transactional
    public void withdrawInterestedCelebByUserId(Long userId) {
        interestedCelebRepository.deleteAllByUserId(userId);
    }
}
