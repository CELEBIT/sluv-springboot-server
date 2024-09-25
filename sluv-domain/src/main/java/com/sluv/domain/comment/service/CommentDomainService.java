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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentDomainService {

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getUserAllLikeComment(User user, Pageable pageable) {
        return commentRepository.getUserAllLikeComment(user, pageable);
    }

    @Transactional(readOnly = true)
    public Long countByQuestionId(Long questionId) {
        return commentRepository.countByQuestionId(questionId);
    }

    @Transactional(readOnly = true)
    public Long countCommentByUserIdInActiveQuestion(Long questionId, CommentStatus commentStatus) {
        return commentRepository.countCommentByUserIdInActiveQuestion(questionId, commentStatus);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getUserAllComment(User user, Pageable pageable) {
        return commentRepository.getUserAllComment(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getAllQuestionComment(Long questionId, Pageable pageable) {
        return commentRepository.getAllQuestionComment(questionId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getAllSubComment(Long commentId, Pageable pageable) {
        return commentRepository.getAllSubComment(commentId, pageable);
    }

    @Transactional
    public Comment saveComment(User user, Question question, String content) {
        Comment comment = Comment.toEntity(user, question, content);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment saveSubComment(User user, Question question, String content, Comment parentComment) {
        Comment comment = Comment.toEntity(user, question, content, parentComment);
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void deleteAllByParentId(Long parentId) {
        commentRepository.deleteAllByParentId(parentId);
    }

}
