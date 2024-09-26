package com.sluv.domain.comment.repository;

import com.sluv.domain.comment.entity.CommentImg;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentImgRepository extends JpaRepository<CommentImg, Long> {
    void deleteAllByCommentId(Long commentId);

    List<CommentImg> findAllByCommentId(Long id);
}
