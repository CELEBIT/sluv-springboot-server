package com.sluv.domain.notice.entity;

import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.notice.enums.NoticeStatus;
import com.sluv.domain.notice.enums.NoticeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notice")
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @NotNull
    @Size(max = 100)
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NoticeStatus Status;

    @Builder
    public Notice(Long id, String title, String content, NoticeType noticeType, NoticeStatus status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.noticeType = noticeType;
        Status = status;
    }
}
