package com.sluv.server.domain.notice.dto;

import com.sluv.server.domain.notice.entity.Notice;
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
    private String title;
    private LocalDateTime createdAt;

    public static NoticeSimpleResDto of(Notice notice){
        return NoticeSimpleResDto.builder()
                .title(notice.getTitle())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
