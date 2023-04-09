package com.sluv.server.domain.celeb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCelebPostResDto {
    private Long newCelebId;
    private String newCelebName;
}
