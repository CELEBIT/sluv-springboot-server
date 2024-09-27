package com.sluv.domain.comment.repository;

import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.repository.impl.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    Long countByQuestionId(Long questionId);

    void deleteAllByParentId(Long commentId);
}
