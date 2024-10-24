package com.sluv.admin.brand.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BrandRegisterRequest {

    private String krName;
    private String enName;
    private String imageUrl;

}
