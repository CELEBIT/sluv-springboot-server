package com.sluv.domain.comment.repository.impl;

import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.enums.CommentStatus;
import com.sluv.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom {
    Page<Comment> getAllQuestionComment(Long questionId, Pageable pageable);

    Page<Comment> getAllSubComment(Long commentId, Pageable pageable);

    Page<Comment> getUserAllLikeComment(User user, List<Long> blockUserIds, Pageable pageable);

    Page<Comment> getUserAllComment(User user, Pageable pageable);

    Long countCommentByUserIdInActiveQuestion(Long userId, CommentStatus commentStatus);

    List<Comment> getAllBlockComment();
}
