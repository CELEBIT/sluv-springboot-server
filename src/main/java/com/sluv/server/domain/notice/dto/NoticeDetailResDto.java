package com.sluv.server.domain.notice.dto;

import com.sluv.server.domain.notice.entity.Notice;
import com.sluv.server.domain.notice.enums.NoticeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDetailResDto {

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private NoticeType noticeType;

    public static NoticeDetailResDto of(Notice notice){

        return NoticeDetailResDto.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .noticeType(notice.getNoticeType())
                .build();
    }
}
