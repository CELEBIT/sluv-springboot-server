package com.sluv.admin.brand.dto;

import com.sluv.domain.brand.entity.NewBrand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewBrandRegisterDto {

    private Long brandId;
    private String name;
    private String createdAt;

    public static NewBrandRegisterDto from(NewBrand newBrand) {
        String dateTimeFormat = newBrand.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM"));
        return NewBrandRegisterDto.builder()
                .brandId(newBrand.getId())
                .name(newBrand.getBrandName())
                .createdAt(dateTimeFormat)
                .build();
    }

}
