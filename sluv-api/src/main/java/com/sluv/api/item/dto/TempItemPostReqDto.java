package com.sluv.api.item.dto;

import com.sluv.domain.item.dto.ItemImgDto;
import com.sluv.domain.item.dto.ItemLinkDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempItemPostReqDto {
    /**
     * 착용사진, 셀럽, 아이템 종류, 브랜드, 상품명, 금액대 날짜, 장소, 추가 정보, 구매 링크
     */

    @Schema(description = "tempItem id(PK)")
    private Long id;
    @Schema(description = "item 이미지 리스트")
    private List<ItemImgDto> imgList;
    @Schema(description = "celebId")
    private Long celebId;
    @Schema(description = "발견 시간 ex)2021-11-20T09:10:20")
    private LocalDateTime whenDiscovery;
    @Schema(description = "발견 장소")
    private String whereDiscovery;
    @Schema(description = "아이템 카테고리 Id")
    private Long categoryId;
    @Schema(description = "브랜드 Id")
    private Long brandId;
    @Schema(description = "아이템명")
    private String itemName;
    @Schema(description = "가격")
    private Integer price;
    @Schema(description = "색")
    private String color;
    @Schema(description = "추가정보")
    private String additionalInfo;
    @Schema(description = "해쉬태그 Id 리스트")
    private List<Long> hashTagIdList;
    @Schema(description = "item 링크 리스트")
    private List<ItemLinkDto> linkList;
    @Schema(description = "추가정보를 발견한 출처")
    private String infoSource;

    @Schema(description = "새로운 Celeb Id")
    private Long newCelebId;
    @Schema(description = "새로운 Brand 이름 Id")
    private Long newBrandId;


}
