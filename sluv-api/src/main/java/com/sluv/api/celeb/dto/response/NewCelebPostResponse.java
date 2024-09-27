package com.sluv.api.celeb.dto.response;

import com.sluv.domain.celeb.entity.NewCeleb;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCelebPostResponse implements Serializable {
    @Schema(description = "생성된 newCeleb의 Id")
    private Long newCelebId;
    @Schema(description = "생성된 newCeleb의 이름")
    private String newCelebName;

    public static NewCelebPostResponse of(NewCeleb newCeleb) {
        return NewCelebPostResponse.builder()
                .newCelebId(newCeleb.getId())
                .newCelebName(newCeleb.getCelebName())
                .build();
    }
}
