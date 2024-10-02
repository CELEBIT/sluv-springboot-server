package com.sluv.api.comment.service;

import com.sluv.api.comment.dto.reponse.CommentResponse;
import com.sluv.api.comment.dto.reponse.SubCommentPageResponse;
import com.sluv.api.comment.dto.request.CommentPostRequest;
import com.sluv.api.comment.helper.CommentHelper;
import com.sluv.api.comment.helper.CommentImgHelper;
import com.sluv.api.comment.helper.CommentItemHelper;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.service.CommentDomainService;
import com.sluv.domain.comment.service.CommentLikeDomainService;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.exception.UserNotMatchedException;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.ai.AiModelService;
import com.sluv.infra.alarm.service.CommentAlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final QuestionDomainService questionDomainService;
    private final CommentDomainService commentDomainService;
    private final CommentLikeDomainService commentLikeDomainService;
    private final UserDomainService userDomainService;

    private final AiModelService aiModelService;
    private final CommentHelper commentHelper;
    private final CommentItemHelper commentItemHelper;
    private final CommentImgHelper commentImgHelper;

    private final CommentAlarmService commentAlarmService;

    /**
     * 댓글 조회
     */
    @Transactional(readOnly = true)
    public PaginationResponse<CommentResponse> getComment(Long userId, Long questionId, Pageable pageable) {
        User user = userDomainService.findById(userId);

        // 해당 페이지 검색
        Page<Comment> commentPage = commentDomainService.getAllQuestionComment(questionId, pageable);

        // Content 제작
        List<CommentResponse> content = commentHelper.getCommentResDtos(user, commentPage.getContent());

        return PaginationResponse.create(commentPage, content);
    }

    /**
     * 댓글 상세 조회
     */
    @Transactional(readOnly = true)
    public CommentResponse getCommentDetail(Long userId, Long commentId) {
        User user = userDomainService.findById(userId);

        // 댓글 조회
        Comment comment = commentDomainService.findById(commentId);

        // Content 제작
        List<CommentResponse> content = commentHelper.getCommentResDtos(user, List.of(comment));

        return content.get(0);

    }

    /**
     * 대댓글 조회
     */
    @Transactional(readOnly = true)
    public SubCommentPageResponse<CommentResponse> getSubComment(Long userId, Long commentId, Pageable pageable) {
        User user = userDomainService.findById(userId);

        // 대댓글 페이지 검색
        Page<Comment> commentPage = commentDomainService.getAllSubComment(commentId, pageable);

        // Content 제작
        List<CommentResponse> content = commentHelper.getCommentResDtos(user, commentPage.getContent());

        // 남은 댓글 수. 총 댓글 수 - ((현재 페이지 +1)*페이지당 size)가 0보다 작으면 0, 아닐 경우 해당 값
        long restCommentNum =
                commentPage.getTotalElements() - ((long) (commentPage.getNumber() + 1) * commentPage.getSize()) >= 0
                        ? commentPage.getTotalElements() - ((long) (commentPage.getNumber() + 1)
                        * commentPage.getSize())
                        : 0;

        return SubCommentPageResponse.of(commentPage, content, restCommentNum);

    }

    /**
     * 댓글 등록
     */
    @Transactional
    public void postComment(Long userId, Long questionId, CommentPostRequest request) {
        User user = userDomainService.findById(userId);

        log.info("댓글 등록 - 사용자 : {}, 질문 게시글 : {}, 댓글 내용 {}", user.getId(), questionId, request.getContent());
        Question question = questionDomainService.findById(questionId);

        Comment comment = commentDomainService.saveComment(user, question, request.getContent());
        commentItemHelper.saveCommentItem(request, comment);
        commentImgHelper.saveCommentImg(request, comment);

        aiModelService.censorComment(comment);
        commentAlarmService.sendAlarmAboutComment(user.getId(), comment.getId());

    }

    /**
     * 대댓글 등록
     */
    @Transactional
    public void postSubComment(Long userId, Long questionId, Long commentId, CommentPostRequest request) {
        User user = userDomainService.findById(userId);

        log.info("대댓글 등록 - 사용자 : {}, 질문 게시글 : {}, 상위 댓글 : {}, 댓글 내용 {}",
                user.getId(), questionId, commentId, request.getContent());

        Question question = questionDomainService.findById(questionId);

        // Parent Comment 조회
        Comment parentComment = commentDomainService.findById(commentId);

        Comment comment = commentDomainService.saveSubComment(user, question, request.getContent(), parentComment);
        commentItemHelper.saveCommentItem(request, comment);
        commentImgHelper.saveCommentImg(request, comment);

        aiModelService.censorComment(comment);
        commentAlarmService.sendAlarmAboutComment(user.getId(), comment.getId());
        commentAlarmService.sendAlarmAboutSubComment(user.getId(), comment.getId());
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public void putComment(Long userId, Long commentId, CommentPostRequest request) {
        /**
         * 1. Comment의 content 수정
         * 2. Comment와 관련된 img 초기화 후 재등록
         * 3. Comment와 관련된 item 초기화 후 재등록
         */
        User user = userDomainService.findById(userId);

        log.info("댓글 수정 - 사용자 : {}. 댓글 : {}", user.getId(), commentId);
        Comment comment = commentDomainService.findById(commentId);

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new UserNotMatchedException();
        }
        // content 변경
        comment.changeContent(request.getContent());

        commentItemHelper.saveCommentItem(request, comment);
        commentImgHelper.saveCommentImg(request, comment);
        aiModelService.censorComment(comment);

    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        User user = userDomainService.findById(userId);

        log.info("댓글 삭제 - 사용자 : {}, 댓글 : {}", user.getId(), commentId);
        Comment comment = commentDomainService.findById(commentId);

        // comment 작성자와 현재 User가 불일치 시 예외 처리
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new UserNotMatchedException();
        }

        // Target Comment 제거
        commentDomainService.deleteById(commentId);

        // Target의 대댓글 제거
        commentDomainService.deleteAllByParentId(commentId);

    }

    /**
     * 댓글 좋아요
     */
    @Transactional
    public void postCommentLike(Long userId, Long commentId) {
        User user = userDomainService.findById(userId);

        log.info("댓글 삭제 - 사용자 : {}, 댓글 : {}", user.getId(), commentId);
        Boolean commentListStatus = commentLikeDomainService.existsByUserIdAndCommentId(user.getId(), commentId);

        if (!commentListStatus) {
            Comment comment = commentDomainService.findById(commentId);
            commentLikeDomainService.saveCommentLike(user, comment);
            commentAlarmService.sendAlarmAboutCommentLike(user.getId(), comment.getId());
        } else {
            commentLikeDomainService.deleteByUserIdAndCommentId(user.getId(), commentId);
        }
    }

}
