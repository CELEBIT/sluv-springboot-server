package com.sluv.server.global.ai.cleanBot;

import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.enums.CommentStatus;
import com.sluv.server.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CleanBotService {
    private final CleanBotRepository cleanBotRepository;
    private final CommentRepository commentRepository;

    @Async(value = "asyncThreadPoolExecutor")
    public void censorComment(Comment comment) {
        boolean isMalicious = cleanBotRepository.isMaliciousComment(comment.getContent());

        if (isMalicious) {
            comment.changeStatus(CommentStatus.BLOCKED);
            commentRepository.save(comment);
        }
    }
}
