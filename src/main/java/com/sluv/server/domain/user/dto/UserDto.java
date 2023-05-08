package com.sluv.server.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    @Schema(description = "사용자 Id")
    private Long id;

    @Builder
    public UserDto(Long id) {
        this.id = id;
    }
}
