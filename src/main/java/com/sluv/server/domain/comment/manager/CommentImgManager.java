package com.sluv.server.domain.comment.manager;

import com.sluv.server.domain.comment.dto.CommentPostReqDto;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.entity.CommentImg;
import com.sluv.server.domain.comment.repository.CommentImgRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentImgManager {
    private final CommentImgRepository commentImgRepository;

    /**
     * 댓글 이미지 등록
     */
    public void saveCommentImg(CommentPostReqDto dto, Comment comment) {
        if (dto.getImgList() != null) {
            // 초기화
            commentImgRepository.deleteAllByCommentId(comment.getId());

            // dto로 부터 새로운 CommentImg 생성
            if (dto.getImgList() != null) {
                List<CommentImg> imgList = dto.getImgList().stream().map(imgUrl ->
                        CommentImg.toEntity(comment, imgUrl)
                ).toList();

                // 저장
                commentImgRepository.saveAll(imgList);
            }
        }
    }


}
