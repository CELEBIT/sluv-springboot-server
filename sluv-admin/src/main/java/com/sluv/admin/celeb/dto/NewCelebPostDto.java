package com.sluv.admin.celeb.dto;

import com.sluv.domain.celeb.entity.NewCeleb;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCelebPostDto {

    private Long newCelebId;
    private String name;
    private String createdAt;

    public static NewCelebPostDto from(NewCeleb newCeleb) {
        String dateTimeFormat = newCeleb.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM"));
        return NewCelebPostDto.builder()
                .newCelebId(newCeleb.getId())
                .name(newCeleb.getCelebName())
                .createdAt(dateTimeFormat)
                .build();
    }

}
