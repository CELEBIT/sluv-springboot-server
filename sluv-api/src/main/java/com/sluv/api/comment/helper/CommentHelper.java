package com.sluv.api.comment.helper;

import com.sluv.api.comment.dto.reponse.CommentItemResponse;
import com.sluv.api.comment.dto.reponse.CommentResponse;
import com.sluv.api.item.helper.ItemHelper;
import com.sluv.domain.comment.dto.CommentImgDto;
import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.repository.CommentImgRepository;
import com.sluv.domain.comment.repository.CommentItemRepository;
import com.sluv.domain.comment.repository.CommentLikeRepository;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentHelper {
    private final CommentImgRepository commentImgRepository;
    private final CommentItemRepository commentItemRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final ItemHelper itemHelper;

    public List<CommentResponse> getCommentResDtos(User user, List<Comment> commentPage) {
        return commentPage
                .stream()
                .map(comment -> {

                    // 해당 Comment에 해당하는 이미지 조회
                    List<CommentImgDto> imgList = commentImgRepository.findAllByCommentId(comment.getId())
//                            .stream().map(CommentImgDto::of)
                            .stream().map(CommentImgDto::of)
                            .toList();
                    // 해당 Comment에 해당하는 아이템 조회
                    List<CommentItemResponse> itemList = commentItemRepository.findAllByCommentId(comment.getId())
                            .stream().map(commentItem -> itemHelper.getCommentItemResDto(commentItem, user))
                            .toList();

                    // 해당 Comment의 좋아요 수
                    Integer likeNum = commentLikeRepository.countByCommentId(comment.getId());
                    // 현재 유저의 해당 Comment 좋아요 여부
                    Boolean likeStatus = user != null && commentLikeRepository.existsByUserIdAndCommentId(user.getId(),
                            comment.getId());

                    User writer = userRepository.findById(comment.getUser().getId()).orElse(null);

                    return CommentResponse.of(comment, writer, user, imgList, itemList, likeNum, likeStatus);
                }).toList();
    }
}
