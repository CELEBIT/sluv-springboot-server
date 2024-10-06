package com.sluv.domain.comment.repository;

import com.sluv.domain.comment.entity.CommentReport;
import com.sluv.domain.comment.repository.impl.CommentReportRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long>, CommentReportRepositoryCustom {
    Boolean existsByReporterIdAndCommentId(Long id, Long commentId);
}
