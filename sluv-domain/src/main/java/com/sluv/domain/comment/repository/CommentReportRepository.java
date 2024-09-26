package com.sluv.domain.comment.repository;

import com.sluv.domain.comment.entity.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
    Boolean existsByReporterIdAndCommentId(Long id, Long commentId);
}
