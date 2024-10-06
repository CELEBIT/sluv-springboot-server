package com.sluv.api.user.service;

import com.sluv.api.comment.dto.reponse.CommentSimpleResponse;
import com.sluv.api.common.response.PaginationCountResponse;
import com.sluv.api.question.mapper.QuestionDtoMapper;
import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.service.CommentDomainService;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
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
public class UserLikeService {

    private final UserDomainService userDomainService;
    private final ItemDomainService itemDomainService;
    private final QuestionDomainService questionDomainService;
    private final CommentDomainService commentDomainService;

    private final QuestionDtoMapper questionDtoMapper;

    @Transactional(readOnly = true)
    public PaginationCountResponse<ItemSimpleDto> getUserLikeItem(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);
        Page<Item> itemPage = itemDomainService.getAllByUserLikeItem(user, pageable);

        List<ItemSimpleDto> content = itemDomainService.getItemSimpleDto(user, itemPage.getContent());

        return new PaginationCountResponse<>(itemPage.hasNext(), itemPage.getNumber(), content,
                itemPage.getTotalElements());
    }

    /**
     * 유저가 좋아요한 Question 게시글 조회
     */
    @Transactional(readOnly = true)
    public PaginationCountResponse<QuestionSimpleResDto> getUserLikeQuestion(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);
        Page<Question> questionPage = questionDomainService.getUserLikeQuestion(user, pageable);

        List<QuestionSimpleResDto> content = questionPage.stream()
                .map(questionDtoMapper::dtoBuildByQuestionType).toList();

        return new PaginationCountResponse<>(questionPage.hasNext(), questionPage.getNumber(), content,
                questionPage.getTotalElements());
    }

    /**
     * 유저가 좋아요한 댓글 목록 조회
     */
    @Transactional(readOnly = true)
    public PaginationCountResponse<CommentSimpleResponse> getUserLikeComment(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);
        Page<Comment> commentPage = commentDomainService.getUserAllLikeComment(user, pageable);

        List<CommentSimpleResponse> content = commentPage.stream().map(CommentSimpleResponse::of).toList();

        return new PaginationCountResponse<>(commentPage.hasNext(), commentPage.getNumber(), content,
                commentPage.getTotalElements());
    }
}
