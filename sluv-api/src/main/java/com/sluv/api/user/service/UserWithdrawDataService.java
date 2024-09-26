package com.sluv.api.user.service;

import com.sluv.api.item.service.TempItemService;
import com.sluv.domain.celeb.service.InterestedCelebDomainService;
import com.sluv.domain.item.service.RecentItemDomainService;
import com.sluv.domain.question.service.RecentQuestionDomainService;
import com.sluv.domain.user.service.FollowDomainService;
import com.sluv.domain.user.service.UserReportStackDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWithdrawDataService {
    private final UserReportStackDomainService userReportStackDomainService;
    private final RecentItemDomainService recentItemDomainService;
    private final TempItemService tempItemService;
    private final RecentQuestionDomainService recentQuestionDomainService;
    private final FollowDomainService followDomainService;
    private final InterestedCelebDomainService interestedCelebDomainService;

    @Async
    @Transactional
    public void withdrawItemByUserId(Long userId) {
        recentItemDomainService.deleteAllByUserId(userId);
        tempItemService.deleteAllTempItem(userId);
    }

    @Async
    @Transactional
    public void withdrawQuestionByUserId(Long userId) {
        recentQuestionDomainService.deleteAllByUserId(userId);
    }

    @Async
    @Transactional
    public void withdrawFollowByUserId(Long userId) {
        followDomainService.deleteFolloweeByUserId(userId);
        followDomainService.deleteFollowerByUserId(userId);
    }

    @Async
    @Transactional
    public void withdrawCelebByUserId(Long userId) {
        interestedCelebDomainService.deleteAllByUserId(userId);
    }

    @Async
    @Transactional
    public void withdrawUserByUserId(Long userId) {
        userReportStackDomainService.deleteAllByReportedId(userId);
    }
}