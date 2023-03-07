package com.sluv.server.domain.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;

    @Builder
    public UserDto(Long id) {
        this.id = id;
    }
}
