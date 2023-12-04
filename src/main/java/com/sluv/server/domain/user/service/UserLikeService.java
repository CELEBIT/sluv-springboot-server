package com.sluv.server.domain.user.service;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.comment.dto.CommentSimpleResDto;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.mapper.ItemDtoMapper;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.mapper.QuestionDtoMapper;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationCountResDto;
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
public class UserLikeService {

    private final ItemRepository itemRepository;
    private final ClosetRepository closetRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final ItemDtoMapper itemDtoMapper;
    private final QuestionDtoMapper questionDtoMapper;

    @Transactional(readOnly = true)
    public PaginationCountResDto<ItemSimpleResDto> getUserLikeItem(User user, Pageable pageable) {
        Page<Item> itemPage = itemRepository.getAllByUserLikeItem(user, pageable);

        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());

        List<ItemSimpleResDto> content = itemPage.stream()
                .map(item -> itemDtoMapper.getItemSimpleResDto(item, closetList)).toList();

        return new PaginationCountResDto<>(itemPage.hasNext(), itemPage.getNumber(), content,
                itemPage.getTotalElements());
    }

    /**
     * 유저가 좋아요한 Question 게시글 조회
     */
    @Transactional(readOnly = true)
    public PaginationCountResDto<QuestionSimpleResDto> getUserLikeQuestion(User user, Pageable pageable) {
        Page<Question> questionPage = questionRepository.getUserLikeQuestion(user, pageable);

        List<QuestionSimpleResDto> content = questionPage.stream()
                .map(questionDtoMapper::dtoBuildByQuestionType).toList();

        return new PaginationCountResDto<>(questionPage.hasNext(), questionPage.getNumber(), content,
                questionPage.getTotalElements());
    }

    /**
     * 유저가 좋아요한 댓글 목록 조회
     */
    @Transactional(readOnly = true)
    public PaginationCountResDto<CommentSimpleResDto> getUserLikeComment(User user, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.getUserAllLikeComment(user, pageable);

        List<CommentSimpleResDto> content = commentPage.stream().map(CommentSimpleResDto::of).toList();

        return new PaginationCountResDto<>(commentPage.hasNext(), commentPage.getNumber(), content,
                commentPage.getTotalElements());
    }
}
