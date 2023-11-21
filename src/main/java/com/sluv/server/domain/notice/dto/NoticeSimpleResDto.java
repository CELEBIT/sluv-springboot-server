package com.sluv.server.domain.notice.dto;

import com.sluv.server.domain.notice.entity.Notice;
import com.sluv.server.domain.notice.enums.NoticeType;
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
public class NoticeSimpleResDto {
    @Schema(description = "공지사항 id")
    private Long id;
    @Schema(description = "공지사항 제목")
    private String title;
    @Schema(description = "공지사항 공지일")
    private LocalDateTime createdAt;
    @Schema(description = "공지사항 타입")
    private NoticeType noticeType;

    public static NoticeSimpleResDto of(Notice notice) {

        return NoticeSimpleResDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .createdAt(notice.getCreatedAt())
                .noticeType(notice.getNoticeType())
                .build();
    }
}
