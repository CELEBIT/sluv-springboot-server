package com.sluv.admin.brand.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewBrandChangeRequest {
    private Long newBrandId;
    private Long brandId;
}
