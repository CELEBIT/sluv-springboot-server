package com.sluv.server.domain.notice.dto;

import com.sluv.server.domain.notice.entity.Notice;
import com.sluv.server.domain.notice.enums.NoticeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeSimpleResDto {
    @Schema(description = "공지사항 제목")
    private String title;
    @Schema(description = "공지사항 공지일")
    private LocalDateTime createdAt;
    @Schema(description = "공지사항 타입")
    private NoticeType noticeType;

    public static NoticeSimpleResDto of(Notice notice){

        return NoticeSimpleResDto.builder()
                .title(notice.getTitle())
                .createdAt(notice.getCreatedAt())
                .noticeType(notice.getNoticeType())
                .build();
    }
}
