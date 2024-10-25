package com.sluv.admin.celeb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@ToString
@NoArgsConstructor
public class CelebSelfPostRequest {

    @Nullable
    private Long parentId;
    private Long categoryId;
    private String krName;
    private String enName;

}
