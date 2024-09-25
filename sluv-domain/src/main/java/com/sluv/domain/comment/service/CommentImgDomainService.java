package com.sluv.domain.comment.service;

import com.sluv.domain.comment.entity.CommentImg;
import com.sluv.domain.comment.repository.CommentImgRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentImgDomainService {

    private final CommentImgRepository commentImgRepository;

    @Transactional
    public void saveAll(List<CommentImg> imgList) {
        commentImgRepository.saveAll(imgList);
    }

    @Transactional
    public void deleteAllByCommentId(Long commentId) {
        commentImgRepository.deleteAllByCommentId(commentId);
    }

}
