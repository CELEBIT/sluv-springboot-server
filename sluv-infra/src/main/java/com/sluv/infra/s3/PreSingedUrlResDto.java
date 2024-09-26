package com.sluv.infra.s3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreSingedUrlResDto {
    private String preSignedUrl;
    private String key;
}
