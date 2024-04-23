package com.sluv.server.domain.comment.handler;

import com.sluv.server.domain.comment.dto.CommentImgDto;
import com.sluv.server.domain.comment.dto.CommentItemResDto;
import com.sluv.server.domain.comment.dto.CommentResDto;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.repository.CommentImgRepository;
import com.sluv.server.domain.comment.repository.CommentItemRepository;
import com.sluv.server.domain.comment.repository.CommentLikeRepository;
import com.sluv.server.domain.item.helper.ItemHelper;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentHelper {
    private final CommentImgRepository commentImgRepository;
    private final CommentItemRepository commentItemRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ItemHelper itemHelper;

    public List<CommentResDto> getCommentResDtos(User user, Page<Comment> commentPage) {
        return commentPage
                .stream()
                .map(comment -> {

                    // 해당 Comment에 해당하는 이미지 조회
                    List<CommentImgDto> imgList = commentImgRepository.findAllByCommentId(comment.getId())
                            .stream().map(CommentImgDto::of)
                            .toList();
                    // 해당 Comment에 해당하는 아이템 조회
                    List<CommentItemResDto> itemList = commentItemRepository.findAllByCommentId(comment.getId())
                            .stream().map(commentItem -> itemHelper.getCommentItemResDto(commentItem, user))
                            .toList();

                    // 해당 Comment의 좋아요 수
                    Integer likeNum = commentLikeRepository.countByCommentId(comment.getId());
                    // 현재 유저의 해당 Comment 좋아요 여부
                    Boolean likeStatus = commentLikeRepository.existsByUserIdAndCommentId(user.getId(),
                            comment.getId());

                    return CommentResDto.of(comment, user, imgList, itemList, likeNum, likeStatus);
                }).toList();
    }
}
