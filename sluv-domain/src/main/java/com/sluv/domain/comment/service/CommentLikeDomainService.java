package com.sluv.domain.comment.service;

import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.entity.CommentLike;
import com.sluv.domain.comment.repository.CommentLikeRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeDomainService {

    private final CommentLikeRepository commentLikeRepository;

    @Transactional(readOnly = true)
    public Boolean existsByUserIdAndCommentId(Long userId, Long commentId) {
        return commentLikeRepository.existsByUserIdAndCommentId(userId, commentId);
    }

    @Transactional
    public void saveCommentLike(User user, Comment comment) {
        CommentLike commentLike = CommentLike.toEntity(user, comment);
        commentLikeRepository.save(commentLike);
    }

    @Transactional
    public void deleteByUserIdAndCommentId(Long userId, Long commentId) {
        commentLikeRepository.deleteByUserIdAndCommentId(userId, commentId);
    }

}
