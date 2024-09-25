package com.sluv.api.comment.helper;

import com.sluv.api.comment.dto.request.CommentPostRequest;
import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.entity.CommentImg;
import com.sluv.domain.comment.service.CommentImgDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentImgHelper {

    private final CommentImgDomainService commentImgDomainService;

    /**
     * 댓글 이미지 등록
     */
    public void saveCommentImg(CommentPostRequest dto, Comment comment) {
        if (dto.getImgList() != null) {
            // 초기화
            commentImgDomainService.deleteAllByCommentId(comment.getId());

            // dto로 부터 새로운 CommentImg 생성
            if (dto.getImgList() != null) {
                List<CommentImg> imgList = dto.getImgList().stream().map(imgUrl ->
                        CommentImg.toEntity(comment, imgUrl)
                ).toList();

                // 저장
                commentImgDomainService.saveAll(imgList);
            }
        }
    }


}
