package com.sluv.server.domain.user.service;

import com.sluv.server.domain.celeb.service.CelebWithdrawService;
import com.sluv.server.domain.closet.service.ClosetWithdrawService;
import com.sluv.server.domain.comment.service.CommentWithdrawService;
import com.sluv.server.domain.item.service.ItemWithdrawService;
import com.sluv.server.domain.question.service.QuestionWithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWithdrawDataService {
    private final ItemWithdrawService itemWithdrawService;
    private final QuestionWithdrawService questionWithdrawService;
    private final CommentWithdrawService commentWithdrawService;
    private final ClosetWithdrawService closetWithdrawService;
    private final FollowWithdrawService followWithdrawService;
    private final CelebWithdrawService celebWithdrawService;
    private final UserWithdrawService userWithdrawService;

    @Async
    @Transactional
    public void withdrawItemByUserId(Long userId) {
        itemWithdrawService.withdrawItemByUserId(userId);
    }

    @Async
    @Transactional
    public void withdrawQuestionByUserId(Long userId) {
        questionWithdrawService.withdrawQuestionByUserId(userId);
    }

    @Async
    @Transactional
    public void withdrawCommentByUserId(Long userId) {
        commentWithdrawService.withdrawCommentByUserId(userId);
    }

    @Async
    @Transactional
    public void withdrawClosetByUserId(Long userId) {
        closetWithdrawService.withdrawClosetByUserId(userId);
    }

    @Async
    @Transactional
    public void withdrawFollowByUserId(Long userId) {
        followWithdrawService.withdrawFollowByUserId(userId);
    }

    @Async
    @Transactional
    public void withdrawCelebByUserId(Long userId) {
        celebWithdrawService.withdrawInterestedCelebByUserId(userId);
    }

    @Async
    @Transactional
    public void withdrawUserByUserId(Long userId) {
        userWithdrawService.withdrawUserByUserId(userId);
    }
}
