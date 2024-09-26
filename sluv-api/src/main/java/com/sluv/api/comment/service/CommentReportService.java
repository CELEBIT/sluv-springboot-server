package com.sluv.api.comment.service;

import com.sluv.api.comment.dto.request.CommentReportPostRequest;
import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.exception.CommentReportDuplicateException;
import com.sluv.domain.comment.service.CommentDomainService;
import com.sluv.domain.comment.service.CommentReportDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentReportService {

    private final CommentDomainService commentDomainService;
    private final CommentReportDomainService commentReportDomainService;
    private final UserDomainService userDomainService;

    /**
     * 댓글 신고
     */
    @Transactional
    public void postCommentReport(Long userId, Long commentId, CommentReportPostRequest request) {
        User user = userDomainService.findById(userId);

        log.info("댓글 신고 - 사용자 : {}, 신고 댓글 : {}", user.getId(), commentId);
        Boolean reportStatus = commentReportDomainService.existsByReporterIdAndCommentId(user.getId(), commentId);

        if (reportStatus) {
            throw new CommentReportDuplicateException();
        }

        Comment comment = commentDomainService.findById(commentId);
        commentReportDomainService.saveCommentReport(comment, user, request.getReason(), request.getContent());
    }

}
