package com.sluv.server.domain.closet.service;

import com.sluv.server.domain.closet.repository.ClosetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClosetWithdrawService {
    private final ClosetRepository closetRepository;

    public void withdrawClosetByUserId(Long userId) {
        closetRepository.withdrawAllByUserId(userId);
    }
}
