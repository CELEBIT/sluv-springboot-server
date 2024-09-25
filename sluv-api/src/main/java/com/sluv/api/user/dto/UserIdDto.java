package com.sluv.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserIdDto {
    @Schema(description = "사용자 Id")
    private Long id;

    public static UserIdDto of(Long userId) {
        return UserIdDto.builder()
                .id(userId)
                .build();
    }
}
