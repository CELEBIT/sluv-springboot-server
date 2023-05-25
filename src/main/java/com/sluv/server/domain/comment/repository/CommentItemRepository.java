package com.sluv.server.domain.comment.repository;

import com.sluv.server.domain.comment.entity.CommentItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentItemRepository extends JpaRepository<CommentItem, Long> {
    void deleteAllByCommentId(Long commentId);
    List<CommentItem> findAllByCommentId(Long id);
}
