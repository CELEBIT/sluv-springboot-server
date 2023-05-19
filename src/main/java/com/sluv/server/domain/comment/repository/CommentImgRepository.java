package com.sluv.server.domain.comment.repository;

import com.sluv.server.domain.comment.entity.CommentImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentImgRepository extends JpaRepository<CommentImg, Long> {
    void deleteAllByCommentId(Long commentId);
}
