package com.sluv.server.domain.comment.service;

import com.sluv.server.domain.alarm.service.CommentAlarmService;
import com.sluv.server.domain.comment.dto.CommentPostReqDto;
import com.sluv.server.domain.comment.dto.CommentResDto;
import com.sluv.server.domain.comment.dto.SubCommentPageResDto;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.entity.CommentLike;
import com.sluv.server.domain.comment.exception.CommentNotFoundException;
import com.sluv.server.domain.comment.handler.CommentHelper;
import com.sluv.server.domain.comment.manager.CommentImgManager;
import com.sluv.server.domain.comment.manager.CommentItemManager;
import com.sluv.server.domain.comment.repository.CommentLikeRepository;
import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.exception.QuestionNotFoundException;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotMatchedException;
import com.sluv.server.global.ai.AiModelService;
import com.sluv.server.global.common.response.PaginationResDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final AiModelService aiModelService;
    private final CommentHelper commentHelper;
    private final CommentItemManager commentItemManager;
    private final CommentImgManager commentImgManager;

    private final CommentAlarmService commentAlarmService;

    /**
     * 댓글 조회
     */
    @Transactional(readOnly = true)
    public PaginationResDto<CommentResDto> getComment(User user, Long questionId, Pageable pageable) {
        // 해당 페이지 검색
        Page<Comment> commentPage = commentRepository.getAllQuestionComment(questionId, pageable);

        // Content 제작
        List<CommentResDto> content = commentHelper.getCommentResDtos(user, commentPage.getContent());

        return PaginationResDto.of(commentPage, content);
    }

    /**
     * 댓글 상세 조회
     */
    @Transactional(readOnly = true)
    public CommentResDto getCommentDetail(User user, Long commentId) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        // Content 제작
        List<CommentResDto> content = commentHelper.getCommentResDtos(user, List.of(comment));

        return content.get(0);

    }

    /**
     * 대댓글 조회
     */
    @Transactional(readOnly = true)
    public SubCommentPageResDto<CommentResDto> getSubComment(User user, Long commentId, Pageable pageable) {
        // 대댓글 페이지 검색
        Page<Comment> commentPage = commentRepository.getAllSubComment(commentId, pageable);

        // Content 제작
        List<CommentResDto> content = commentHelper.getCommentResDtos(user, commentPage.getContent());

        // 남은 댓글 수. 총 댓글 수 - ((현재 페이지 +1)*페이지당 size)가 0보다 작으면 0, 아닐 경우 해당 값
        long restCommentNum =
                commentPage.getTotalElements() - ((long) (commentPage.getNumber() + 1) * commentPage.getSize()) >= 0
                        ? commentPage.getTotalElements() - ((long) (commentPage.getNumber() + 1)
                        * commentPage.getSize())
                        : 0;

        return SubCommentPageResDto.of(commentPage, content, restCommentNum);

    }

    /**
     * 댓글 등록
     */
    @Transactional
    public void postComment(User user, Long questionId, CommentPostReqDto dto) {
        log.info("댓글 등록 - 사용자 : {}, 질문 게시글 : {}, 댓글 내용 {}", user.getId(), questionId, dto.getContent());
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        Comment comment = commentRepository.save(Comment.toEntity(user, question, dto.getContent()));
        commentItemManager.saveCommentItem(dto, comment);
        commentImgManager.saveCommentImg(dto, comment);

        aiModelService.censorComment(comment);
        commentAlarmService.sendAlarmAboutComment(user.getId(), comment.getId());

    }

    /**
     * 대댓글 등록
     */
    @Transactional
    public void postSubComment(User user, Long questionId, Long commentId, CommentPostReqDto dto) {
        log.info("대댓글 등록 - 사용자 : {}, 질문 게시글 : {}, 상위 댓글 : {}, 댓글 내용 {}",
                user.getId(), questionId, commentId, dto.getContent());

        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        // Parent Comment 조회
        Comment parentComment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        Comment comment = commentRepository.save(Comment.toEntity(user, question, dto.getContent(), parentComment));
        commentItemManager.saveCommentItem(dto, comment);
        commentImgManager.saveCommentImg(dto, comment);

        aiModelService.censorComment(comment);
        commentAlarmService.sendAlarmAboutComment(user.getId(), comment.getId());
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public void putComment(User user, Long commentId, CommentPostReqDto dto) {
        /**
         * 1. Comment의 content 수정
         * 2. Comment와 관련된 img 초기화 후 재등록
         * 3. Comment와 관련된 item 초기화 후 재등록
         */
        log.info("댓글 수정 - 사용자 : {}. 댓글 : {}", user.getId(), commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new UserNotMatchedException();
        }
        // content 변경
        comment.changeContent(dto.getContent());

        commentItemManager.saveCommentItem(dto, comment);
        commentImgManager.saveCommentImg(dto, comment);
        aiModelService.censorComment(comment);

    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(User user, Long commentId) {
        log.info("댓글 삭제 - 사용자 : {}, 댓글 : {}", user.getId(), commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        // comment 작성자와 현재 User가 불일치 시 예외 처리
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new UserNotMatchedException();
        }

        // Target Comment 제거
        commentRepository.deleteById(commentId);

        // Target의 대댓글 제거
        commentRepository.deleteAllByParentId(commentId);

    }

    /**
     * 댓글 좋아요
     */
    @Transactional
    public void postCommentLike(User user, Long commentId) {
        log.info("댓글 삭제 - 사용자 : {}, 댓글 : {}", user.getId(), commentId);
        Boolean commentListStatus = commentLikeRepository.existsByUserIdAndCommentId(user.getId(), commentId);

        if (!commentListStatus) {
            Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
            commentLikeRepository.save(CommentLike.toEntity(user, comment));
            commentAlarmService.sendAlarmAboutCommentLike(user.getId(), comment.getId());
        } else {
            commentLikeRepository.deleteByUserIdAndCommentId(user.getId(), commentId);
        }
    }


}
