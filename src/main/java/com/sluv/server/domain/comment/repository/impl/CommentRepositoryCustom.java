package com.sluv.server.domain.comment.repository.impl;

import com.sluv.server.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<Comment> getAllQuestionComment(Long questionId, Pageable pageable);

    Page<Comment> getAllSubComment(Long commentId, Pageable pageable);
}
