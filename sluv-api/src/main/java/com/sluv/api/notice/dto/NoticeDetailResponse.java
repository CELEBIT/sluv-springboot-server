package com.sluv.api.notice.dto;

import com.sluv.domain.notice.entity.Notice;
import com.sluv.domain.notice.enums.NoticeType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDetailResponse {

    @Schema(description = "공지사항 제목")
    private String title;
    @Schema(description = "공지사항 내용")
    private String content;
    @Schema(description = "공지사항 공지일")
    private LocalDateTime createdAt;
    @Schema(description = "공지사항 타입")
    private NoticeType noticeType;

    public static NoticeDetailResponse of(Notice notice) {

        return NoticeDetailResponse.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .noticeType(notice.getNoticeType())
                .build();
    }
}
