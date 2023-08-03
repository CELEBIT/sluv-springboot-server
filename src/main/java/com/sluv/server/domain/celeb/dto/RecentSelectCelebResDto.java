package com.sluv.server.domain.celeb.dto;

import com.sluv.server.domain.celeb.entity.RecentSelectCeleb;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentSelectCelebResDto {
    @Schema(description = "child 셀럽의 Id")
    private Long id;
    @Schema(description = "parent 셀럽 Id")
    private Long parentId;
    @Schema(description = "child 셀럽의 이름")
    private String childCelebName;
    @Schema(description = "parent 셀럽의 이름")
    private String parentCelebName;
    @Schema(description = "셀럽(Y)과 뉴셀럽(N)을 구분하는 플래그")
    private String flag;

    public static RecentSelectCelebResDto of(RecentSelectCeleb recentSelectCeleb){
        Long celebChildId;
        Long celebParentId;
        String celebChildName;
        String celebParentName;

        String flag = recentSelectCeleb.getCeleb() != null ? "Y" :"N";
        if(flag.equals("Y")){
            celebChildId = recentSelectCeleb.getCeleb().getId();
            celebParentId = recentSelectCeleb.getCeleb().getParent() != null
                    ? recentSelectCeleb.getCeleb().getParent().getId()
                    : null;
            celebChildName = recentSelectCeleb.getCeleb().getCelebNameKr();
            celebParentName = recentSelectCeleb.getCeleb().getParent() != null
                    ? recentSelectCeleb.getCeleb().getParent().getCelebNameKr()
                    : null;
        }else{
            celebChildId = recentSelectCeleb.getNewCeleb().getId();
            celebParentId = null;
            celebChildName = recentSelectCeleb.getNewCeleb().getCelebName();
            celebParentName = null;
        }
        return RecentSelectCelebResDto.builder()
                .id(celebChildId)
                .parentId(celebParentId)
                .childCelebName(celebChildName)
                .parentCelebName(celebParentName)
                .flag(flag)
                .build();
    }
}
