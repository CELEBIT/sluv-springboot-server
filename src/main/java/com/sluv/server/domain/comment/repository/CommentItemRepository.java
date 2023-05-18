package com.sluv.server.domain.comment.repository;

import com.sluv.server.domain.comment.entity.CommentItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentItemRepository extends JpaRepository<CommentItem, Long> {
}
