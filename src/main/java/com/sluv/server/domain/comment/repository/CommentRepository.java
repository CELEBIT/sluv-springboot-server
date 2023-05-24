package com.sluv.server.domain.comment.repository;

import com.sluv.server.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByQuestionId(Long questionId);

    void deleteAllByParentId(Long commentId);
}
