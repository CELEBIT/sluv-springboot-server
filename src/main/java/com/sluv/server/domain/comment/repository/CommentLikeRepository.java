package com.sluv.server.domain.comment.repository;

import com.sluv.server.domain.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Boolean existsByUserIdAndCommentId(Long id, Long commentId);

    void deleteByUserIdAndCommentId(Long id, Long commentId);

    Integer countByCommentId(Long id);
}
