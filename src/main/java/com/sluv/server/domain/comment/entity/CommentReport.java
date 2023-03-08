package com.sluv.server.domain.comment.entity;

import com.sluv.server.domain.question.enums.comment.CommentReportReason;
import com.sluv.server.domain.user.enums.UserReportReason;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "comment_report")
public class CommentReport extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_report_id")
    private Long id;

    @NotNull
    private Long commentId;

    @NotNull
    private Long reporterId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Size(max = 45)
    private CommentReportReason commentReportReason;

    @Size(max = 1002)
    private String content;

    @Builder
    public CommentReport(Long id, Long commentId, Long reporterId, CommentReportReason commentReportReason, String content) {
        this.id = id;
        this.commentId = commentId;
        this.reporterId = reporterId;
        this.commentReportReason = commentReportReason;
        this.content = content;
    }
}
