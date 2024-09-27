package com.sluv.domain.comment.service;

import com.sluv.domain.comment.entity.CommentImg;
import com.sluv.domain.comment.repository.CommentImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentImgDomainService {

    private final CommentImgRepository commentImgRepository;

    public void saveAll(List<CommentImg> imgList) {
        commentImgRepository.saveAll(imgList);
    }

    public void deleteAllByCommentId(Long commentId) {
        commentImgRepository.deleteAllByCommentId(commentId);
    }

}
