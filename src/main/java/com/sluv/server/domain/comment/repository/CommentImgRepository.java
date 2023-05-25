package com.sluv.server.domain.comment.repository;

import com.sluv.server.domain.comment.entity.CommentImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentImgRepository extends JpaRepository<CommentImg, Long> {
    void deleteAllByCommentId(Long commentId);

    List<CommentImg> findAllByCommentId(Long id);
}
