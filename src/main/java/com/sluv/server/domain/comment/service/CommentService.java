package com.sluv.server.domain.comment.service;

import com.sluv.server.domain.comment.dto.CommentPostReqDto;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.entity.CommentImg;
import com.sluv.server.domain.comment.entity.CommentItem;
import com.sluv.server.domain.comment.enums.CommentStatus;
import com.sluv.server.domain.comment.repository.CommentImgRepository;
import com.sluv.server.domain.comment.repository.CommentItemRepository;
import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.exception.QuestionNotFoundException;
import com.sluv.server.domain.question.repository.QuestionRepository;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentImgRepository commentImgRepository;
    private final CommentItemRepository commentItemRepository;
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
        List<CommentImg> imgList = dto.getImgList().stream().map(imgUrl ->
                CommentImg.builder()
                        .comment(comment)
                        .imgUrl(imgUrl)
                        .build()
        ).toList();

        commentImgRepository.saveAll(imgList);

        // 3. CommentItem 등록
        List<CommentItem> itemList = dto.getItemList().stream().map(itemId -> {
                    Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
                    return CommentItem.builder()
                            .comment(comment)
                            .item(item)
                            .build();
                }
        ).toList();

        commentItemRepository.saveAll(itemList);

    }
}
