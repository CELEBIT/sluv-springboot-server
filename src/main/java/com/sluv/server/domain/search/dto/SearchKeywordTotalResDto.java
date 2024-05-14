package com.sluv.server.domain.search.dto;

import com.sluv.server.domain.brand.dto.BrandSearchResDto;
import com.sluv.server.domain.celeb.dto.CelebSearchResDto;
import com.sluv.server.domain.item.dto.ItemKeywordSearchResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchKeywordTotalResDto {
    @Schema(description = "조회된 Celeb 리스트")
    List<CelebSearchResDto> celebList;
    @Schema(description = "조회된 Brand 리스트")
    List<BrandSearchResDto> brandList;
    @Schema(description = "조회된 Item 리스트")
    List<ItemKeywordSearchResDto> itemList;

    public static SearchKeywordTotalResDto of(List<CelebSearchResDto> celebList,
                                              List<BrandSearchResDto> brandList,
                                              List<ItemKeywordSearchResDto> itemList) {
        return SearchKeywordTotalResDto.builder()
                .celebList(celebList)
                .brandList(brandList)
                .itemList(itemList)
                .build();
    }
}
