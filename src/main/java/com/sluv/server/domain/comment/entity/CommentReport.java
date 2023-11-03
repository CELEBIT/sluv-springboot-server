package com.sluv.server.domain.comment.entity;

import com.sluv.server.domain.comment.dto.CommentReportPostReqDto;
import com.sluv.server.domain.comment.enums.CommentReportReason;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import com.sluv.server.global.common.enums.ReportStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "comment_report")
public class CommentReport extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @NotNull
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    @NotNull
    private User reporter;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CommentReportReason commentReportReason;

    @Size(max = 1002)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'WAITING'")
    private ReportStatus reportStatus;

    public static CommentReport toEntity(Comment comment, User user, CommentReportPostReqDto dto){
        return CommentReport.builder()
                .comment(comment)
                .reporter(user)
                .commentReportReason(dto.getReason())
                .content(dto.getContent())
                .reportStatus(ReportStatus.WAITING)
                .build();
    }
}
