package com.sluv.server.domain.user.service;

import com.sluv.server.domain.celeb.repository.InterestedCelebRepository;
import com.sluv.server.domain.item.repository.RecentItemRepository;
import com.sluv.server.domain.item.service.TempItemService;
import com.sluv.server.domain.question.repository.RecentQuestionRepository;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserReportStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWithdrawDataService {
    private final UserReportStackRepository userReportStackRepository;
    private final RecentItemRepository recentItemRepository;
    private final TempItemService tempItemService;
    private final RecentQuestionRepository recentQuestionRepository;
    private final FollowRepository followRepository;
    private final InterestedCelebRepository interestedCelebRepository;

    @Async
    @Transactional
    public void withdrawItemByUserId(Long userId) {
        recentItemRepository.deleteAllByUserId(userId);
        tempItemService.deleteAllTempItem(userId);
    }

    @Async
    @Transactional
    public void withdrawQuestionByUserId(Long userId) {
        recentQuestionRepository.deleteAllByUserId(userId);
    }

    @Async
    @Transactional
    public void withdrawFollowByUserId(Long userId) {
        followRepository.deleteFolloweeByUserId(userId);
        followRepository.deleteFollowerByUserId(userId);
    }

    @Async
    @Transactional
    public void withdrawCelebByUserId(Long userId) {
        interestedCelebRepository.deleteAllByUserId(userId);
    }

    @Async
    @Transactional
    public void withdrawUserByUserId(Long userId) {
        userReportStackRepository.deleteAllByReportedId(userId);
    }
}