package com.sluv.domain.item.dto;

public record ItemStatusDto(
        Boolean likeStatus,
        Boolean scrapStatus,
        Boolean followStatus
) {
}
