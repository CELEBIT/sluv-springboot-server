package com.sluv.server.domain.closet.dto;

import com.sluv.server.domain.closet.enums.ClosetStatus;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ClosetDetailResDto<T> extends PaginationResDto<T> {
    private String coverImgUrl;
    private String title;
    private ClosetStatus closetStatus;
    private Long itemNum;

    public ClosetDetailResDto(Boolean hasNext, Integer page, List<T> content, String coverImgUrl, String title, ClosetStatus closetStatus, Long itemNum) {
        super(hasNext, page, content);
        this.coverImgUrl = coverImgUrl;
        this.title = title;
        this.closetStatus = closetStatus;
        this.itemNum = itemNum;
    }
}
