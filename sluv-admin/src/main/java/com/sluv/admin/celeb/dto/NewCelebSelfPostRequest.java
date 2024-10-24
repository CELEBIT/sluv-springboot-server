package com.sluv.admin.celeb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor
public class NewCelebSelfPostRequest {

    private Long newCelebId;
    @Nullable
    private Long parentId;
    private Long categoryId;
    private String krName;
    private String enName;

}
