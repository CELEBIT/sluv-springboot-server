package com.sluv.server.domain.comment.service;

import com.sluv.server.domain.comment.dto.CommentPostReqDto;
import com.sluv.server.domain.comment.dto.CommentReportPostReqDto;
import com.sluv.server.domain.comment.entity.*;
import com.sluv.server.domain.comment.enums.CommentStatus;
import com.sluv.server.domain.comment.exception.CommentNotFoundException;
import com.sluv.server.domain.comment.exception.CommentReportDuplicateException;
import com.sluv.server.domain.comment.repository.*;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.exception.QuestionNotFoundException;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotMatchedException;
import com.sluv.server.global.common.enums.ReportStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentImgRepository commentImgRepository;
    private final CommentItemRepository commentItemRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentReportRepository commentReportRepository;
    private final QuestionRepository questionRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void postComment(User user, Long questionId, CommentPostReqDto dto){
        /**
         *  1. Comment 등록
         *  2. CommentImg 등록
         *  3. CommentItem 등록
         */
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);


        // 1. Comment 등록
        Comment comment = commentRepository.save(
                Comment.builder()
                        .user(user)
                        .question(question)
                        .content(dto.getContent())
                        .commentStatus(CommentStatus.ACTIVE)
                        .build()
        );

        // 2. CommentImg 등록
        saveCommentImg(dto, comment);

        // 3. CommentItem 등록
        saveCommentItem(dto, comment);

    }

    @Transactional
    public void postNestedComment(User user, Long questionId, Long commentId, CommentPostReqDto dto) {
        /**
         *  1. Comment 등록
         *  2. CommentImg 등록
         *  3. CommentItem 등록
         */
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        // 1. Comment 등록
        // Parent Comment 조회
        Comment parentComment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        Comment comment = commentRepository.save(
                Comment.builder()
                        .user(user)
                        .question(question)
                        .content(dto.getContent())
                        .parent(parentComment)
                        .commentStatus(CommentStatus.ACTIVE)
                        .build()
        );

        // 2. CommentImg 등록
        saveCommentImg(dto, comment);

        // 3. CommentItem 등록
        saveCommentItem(dto, comment);
    }

    @Transactional
    public void putComment(User user, Long commentId, CommentPostReqDto dto) {
        /**
         * 1. Comment의 content 수정
         * 2. Comment와 관련된 img 초기화 후 재등록
         * 3. Comment와 관련된 item 초기화 후 재등록
         */
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        if(!comment.getUser().getId().equals(user.getId())) {
            throw new UserNotMatchedException();
        }
            // content 변경
            comment.changeContent(dto.getContent());

            // img 변경
            saveCommentImg(dto, comment);

            // item 변경
            saveCommentItem(dto, comment);


    }

    private void saveCommentItem(CommentPostReqDto dto, Comment comment) {
        // 초기화
        commentItemRepository.deleteAllByCommentId(comment.getId());

        // dto로 부터 새로운 CommentItem 생성
        List<CommentItem> itemList = dto.getItemList().stream().map(itemId -> {
                    Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
                    return CommentItem.builder()
                            .comment(comment)
                            .item(item)
                            .build();
                }
        ).toList();

        // 저장
        commentItemRepository.saveAll(itemList);
    }

    private void saveCommentImg(CommentPostReqDto dto, Comment comment) {
        // 초기화
        commentImgRepository.deleteAllByCommentId(comment.getId());

        // dto로 부터 새로운 CommentImg 생성
        List<CommentImg> imgList = dto.getImgList().stream().map(imgUrl ->
                CommentImg.builder()
                        .comment(comment)
                        .imgUrl(imgUrl)
                        .build()
        ).toList();

        // 저장
        commentImgRepository.saveAll(imgList);
    }


    @Transactional
    public void postCommentLike(User user, Long commentId) {
        Boolean commentListStatus = commentLikeRepository.existsByUserIdAndCommentId(user.getId(), commentId);

        if(!commentListStatus){
            Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
            commentLikeRepository.save(
                    CommentLike.builder()
                            .user(user)
                            .comment(comment)
                            .build()
            );
        }else{
            commentLikeRepository.deleteByUserIdAndCommentId(user.getId(), commentId);
        }
    }

    public void postCommentReport(User user, Long commentId, CommentReportPostReqDto dto) {
        Boolean reportStatus = commentReportRepository.existsByReporterIdAndCommentId(user.getId(), commentId);

        if(reportStatus) {
          throw new CommentReportDuplicateException();
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        commentReportRepository.save(
                CommentReport.builder()
                        .reporter(user)
                        .comment(comment)
                        .commentReportReason(dto.getReportReason())
                        .content(dto.getContent())
                        .reportStatus(ReportStatus.WAITING)
                        .build()
        );
    }

    @Transactional
    public void deleteComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        // comment 작성자와 현재 User가 불일치 시 예외 처리
        if(!comment.getUser().getId().equals(user.getId())){
            throw new UserNotMatchedException();
        }

        // Target Comment 제거
        commentRepository.deleteById(commentId);

        // Target의 대댓글 제거
        commentRepository.deleteAllByParentId(commentId);

    }
}
