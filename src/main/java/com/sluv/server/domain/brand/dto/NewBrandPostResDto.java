package com.sluv.server.domain.brand.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewBrandPostResDto {
    private Long newBrandId;
    private String newBrandName;
}
