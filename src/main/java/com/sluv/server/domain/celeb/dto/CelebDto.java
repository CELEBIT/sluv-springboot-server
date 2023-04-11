package com.sluv.server.domain.celeb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CelebDto {
    private Long id;
    private String celebNameKr;
    private String celebNameEn;
    private String categoryChild;
    private String categoryParent;

    private String parentCelebNameKr;
    private String parentCelebNameEn;



}
