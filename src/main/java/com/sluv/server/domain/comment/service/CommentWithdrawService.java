package com.sluv.server.domain.comment.service;

import com.sluv.server.domain.comment.repository.CommentLikeRepository;
import com.sluv.server.domain.comment.repository.CommentReportRepository;
import com.sluv.server.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentWithdrawService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentReportRepository commentReportRepository;

    public void withdrawCommentByUserId(Long userId) {
        commentRepository.withdrawByUserId(userId);
        commentLikeRepository.withdrawByUserId(userId);
        commentReportRepository.withdrawByUserId(userId);
    }
}
