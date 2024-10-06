package com.sluv.admin.comment.service;

import com.sluv.admin.comment.dto.CommentBlockCountResponse;
import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.service.CommentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentDomainService commentDomainService;

    public CommentBlockCountResponse getCommentBlockCount() {
        LocalDateTime now = LocalDateTime.now();
        List<Comment> allBlackComment = commentDomainService.getAllBlockComment();
        long recentMonthCount = getRecentMonthCount(now, allBlackComment);
        List<Long> countGraph = getCountGraph(now, allBlackComment);
        return CommentBlockCountResponse.of(allBlackComment.stream().count(), recentMonthCount, countGraph);
    }

    private static List<Long> getCountGraph(LocalDateTime now, List<Comment> allBlockComment) {
        List<Long> countGraph = new ArrayList<>();
        for (int i = 10; i > 0; i--) {
            LocalDateTime startWeek = now.minusWeeks(i);
            LocalDateTime endWeek = now.minusWeeks(i - 1);
            long count = allBlockComment.stream()
                    .filter(comment ->
                            comment.getCreatedAt().isAfter(startWeek) && comment.getCreatedAt().isBefore(endWeek)
                    )
                    .count();
            countGraph.add(count);
        }
        return countGraph;
    }

    private static long getRecentMonthCount(LocalDateTime now, List<Comment> allBlockComment) {
        return allBlockComment.stream()
                .filter(comment -> comment.getCreatedAt().isAfter(now.minusMonths(1)))
                .count();
    }
}
