package com.sluv.admin.brand.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class NewBrandRegisterRequest {

    private Long newBrandId;
    private String krName;
    private String enName;
    private String imageUrl;

}
