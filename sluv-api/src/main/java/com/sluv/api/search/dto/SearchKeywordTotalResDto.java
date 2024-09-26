package com.sluv.api.search.dto;

import com.sluv.api.brand.dto.response.BrandSearchResponse;
import com.sluv.api.celeb.dto.response.CelebSearchResponse;
import com.sluv.api.item.dto.ItemKeywordSearchResDto;
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
    List<CelebSearchResponse> celebList;
    @Schema(description = "조회된 Brand 리스트")
    List<BrandSearchResponse> brandList;
    @Schema(description = "조회된 Item 리스트")
    List<ItemKeywordSearchResDto> itemList;

    public static SearchKeywordTotalResDto of(List<CelebSearchResponse> celebList,
                                              List<BrandSearchResponse> brandList,
                                              List<ItemKeywordSearchResDto> itemList) {
        return SearchKeywordTotalResDto.builder()
                .celebList(celebList)
                .brandList(brandList)
                .itemList(itemList)
                .build();
    }
}
