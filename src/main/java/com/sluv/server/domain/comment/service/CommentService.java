package com.sluv.server.domain.comment.service;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.comment.dto.*;
import com.sluv.server.domain.comment.entity.*;
import com.sluv.server.domain.comment.enums.CommentStatus;
import com.sluv.server.domain.comment.exception.CommentNotFoundException;
import com.sluv.server.domain.comment.exception.CommentReportDuplicateException;
import com.sluv.server.domain.comment.repository.*;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.exception.QuestionNotFoundException;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.user.dto.UserInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotMatchedException;
import com.sluv.server.global.common.enums.ReportStatus;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final ItemImgRepository itemImgRepository;
    private final ItemScrapRepository itemScrapRepository;
    private final ClosetRepository closetRepository;

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
                Comment.toEntity(user, question, dto.getContent())
        );

        // 2. CommentImg 등록
        if(dto.getImgList() != null) {
            saveCommentImg(dto, comment);
        }

        // 3. CommentItem 등록
        if(dto.getItemList() != null) {
            saveCommentItem(dto, comment);
        }

    }

    @Transactional
    public void postSubComment(User user, Long questionId, Long commentId, CommentPostReqDto dto) {
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
                Comment.toEntity(user, question, dto.getContent(), parentComment)
        );

        // 2. CommentImg 등록
        if(dto.getImgList() != null) {
            saveCommentImg(dto, comment);
        }

        // 3. CommentItem 등록
        if(dto.getItemList() != null) {
            saveCommentItem(dto, comment);
        }
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
        if(dto.getItemList() != null) {
            // dto로 부터 새로운 CommentItem 생성
            List<CommentItem> itemList = dto.getItemList().stream().map(itemReqDto -> {
                        Item item = itemRepository.findById(itemReqDto.getItemId()).orElseThrow(ItemNotFoundException::new);
                        return CommentItem.toEntity(comment, item, itemReqDto);
                    }
            ).toList();

            // 저장
            commentItemRepository.saveAll(itemList);
        }
    }

    private void saveCommentImg(CommentPostReqDto dto, Comment comment) {
        // 초기화
        commentImgRepository.deleteAllByCommentId(comment.getId());

        // dto로 부터 새로운 CommentImg 생성
        if(dto.getImgList() != null) {
            List<CommentImg> imgList = dto.getImgList().stream().map(imgUrl ->
                    CommentImg.toEntity(comment, imgUrl)
            ).toList();

            // 저장
            commentImgRepository.saveAll(imgList);
        }
    }


    @Transactional
    public void postCommentLike(User user, Long commentId) {
        Boolean commentListStatus = commentLikeRepository.existsByUserIdAndCommentId(user.getId(), commentId);

        if(!commentListStatus){
            Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
            commentLikeRepository.save(
                    CommentLike.toEntity(user, comment)
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
                CommentReport.toEntity(comment, user, dto)
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

    public PaginationResDto<CommentResDto> getComment(User user, Long questionId, Pageable pageable) {
        // 해당 페이지 검색
        Page<Comment> commentPage = commentRepository.getAllQuestionComment(questionId, pageable);

        // Content 제작
        List<CommentResDto> content = getCommentResDtos(user, commentPage);


        return PaginationResDto.<CommentResDto>builder()
                .page(commentPage.getNumber())
                .hasNext(commentPage.hasNext())
                .content(content)
                .build();
    }

    public SubCommentPageResDto<CommentResDto> getSubComment(User user, Long commentId, Pageable pageable) {
        // 대댓글 페이지 검색
        Page<Comment> commentPage = commentRepository.getAllSubComment(commentId, pageable);

        // Content 제작
        List<CommentResDto> content = getCommentResDtos(user, commentPage);

        // 남은 댓글 수. 총 댓글 수 - ((현재 페이지 +1)*페이지당 size)가 0보다 작으면 0, 아닐 경우 해당 값
        long restCommentNum = commentPage.getTotalElements() - ((long) (commentPage.getNumber() + 1) * commentPage.getSize()) >= 0
                ? commentPage.getTotalElements() - ((long) (commentPage.getNumber() + 1) * commentPage.getSize())
                : 0;

        return SubCommentPageResDto.of(commentPage, content, restCommentNum);

    }

    private List<CommentResDto> getCommentResDtos(User user, Page<Comment> commentPage) {
        return commentPage
                .stream()
                .map(comment -> {

                    // 해당 Comment에 해당하는 이미지 조회
                    List<CommentImgDto> imgList = commentImgRepository.findAllByCommentId(comment.getId())
                            .stream().map(CommentImgDto::of)
                            .toList();
                    // 해당 Comment에 해당하는 아이템 조회
                    List<CommentItemResDto> itemList = commentItemRepository.findAllByCommentId(comment.getId())
                            .stream().map(commentItem -> getCommentItemResDto(commentItem, user))
                            .toList();

                    // 해당 Comment의 좋아요 수
                    Integer likeNum = commentLikeRepository.countByCommentId(comment.getId());
                    // 현재 유저의 해당 Comment 좋아요 여부
                    Boolean likeStatus = commentLikeRepository.existsByUserIdAndCommentId(user.getId(), comment.getId());

                    return CommentResDto.of(comment, user, imgList, itemList, likeNum, likeStatus);
                }).toList();
    }

    /**
     * CommentItem -> CommentItemResDto 변경하는 메소드
     * @param commentItem
     * @return CommentItemResDto
     */
    private CommentItemResDto getCommentItemResDto(CommentItem commentItem, User user) {
        ItemImg mainImg = itemImgRepository.findMainImg(commentItem.getItem().getId());
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());
        Boolean itemScrapStatus = itemScrapRepository.getItemScrapStatus(commentItem.getItem(), closetList);

        return CommentItemResDto.of(commentItem, mainImg, itemScrapStatus);
    }


}
