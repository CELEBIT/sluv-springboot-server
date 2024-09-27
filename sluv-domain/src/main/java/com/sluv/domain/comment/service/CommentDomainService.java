package com.sluv.domain.comment.service;

import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.enums.CommentStatus;
import com.sluv.domain.comment.exception.CommentNotFoundException;
import com.sluv.domain.comment.repository.CommentRepository;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDomainService {

    private final CommentRepository commentRepository;

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }

    public Page<Comment> getUserAllLikeComment(User user, Pageable pageable) {
        return commentRepository.getUserAllLikeComment(user, pageable);
    }

    public Long countByQuestionId(Long questionId) {
        return commentRepository.countByQuestionId(questionId);
    }

    public Long countCommentByUserIdInActiveQuestion(Long questionId, CommentStatus commentStatus) {
        return commentRepository.countCommentByUserIdInActiveQuestion(questionId, commentStatus);
    }

    public Page<Comment> getUserAllComment(User user, Pageable pageable) {
        return commentRepository.getUserAllComment(user, pageable);
    }

    public Page<Comment> getAllQuestionComment(Long questionId, Pageable pageable) {
        return commentRepository.getAllQuestionComment(questionId, pageable);
    }

    public Page<Comment> getAllSubComment(Long commentId, Pageable pageable) {
        return commentRepository.getAllSubComment(commentId, pageable);
    }

    public Comment saveComment(User user, Question question, String content) {
        Comment comment = Comment.toEntity(user, question, content);
        return commentRepository.save(comment);
    }

    public Comment saveSubComment(User user, Question question, String content, Comment parentComment) {
        Comment comment = Comment.toEntity(user, question, content, parentComment);
        return commentRepository.save(comment);
    }

    public void deleteById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void deleteAllByParentId(Long parentId) {
        commentRepository.deleteAllByParentId(parentId);
    }

}
