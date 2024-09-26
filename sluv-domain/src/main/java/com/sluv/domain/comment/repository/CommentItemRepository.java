package com.sluv.domain.comment.repository;

import com.sluv.domain.comment.entity.CommentItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentItemRepository extends JpaRepository<CommentItem, Long> {
    void deleteAllByCommentId(Long commentId);

    List<CommentItem> findAllByCommentId(Long id);
}
