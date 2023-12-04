package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.brand.dto.NewBrandPostResDto;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.celeb.dto.CelebDto;
import com.sluv.server.domain.celeb.dto.NewCelebPostResDto;
import com.sluv.server.domain.item.entity.TempItem;
import com.sluv.server.domain.item.entity.hashtag.Hashtag;
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
public class TempItemResDto {
    /**
     * 착용사진, 셀럽, 아이템 종류, 브랜드, 상품명, 금액대 날짜, 장소, 추가 정보, 구매 링크
     */

    @Schema(description = "item Id")
    private Long id;
    @Schema(description = "item 이미지 리스트")
    private List<ItemImgResDto> imgList;
    @Schema(description = "celeb")
    private CelebDto celeb;
    @Schema(description = "발견 시간 ex)2021-11-20T09:10:20")
    private LocalDateTime whenDiscovery;
    @Schema(description = "발견 장소")
    private String whereDiscovery;
    @Schema(description = "아이템 카테고리")
    private ItemCategoryDto category;
    @Schema(description = "브랜드")
    private Brand brand;
    @Schema(description = "아이템명")
    private String itemName;
    @Schema(description = "가격")
    private Integer price;
    @Schema(description = "추가정보")
    private String additionalInfo;
    @Schema(description = "해쉬태그 리스트")
    private List<Hashtag> hashTagList;
    @Schema(description = "item 링크 리스트 ")
    private List<ItemLinkResDto> linkList;
    @Schema(description = "추가정보를 발견한 출처")
    private String infoSource;

    @Schema(description = "새로운 Celeb")
    private NewCelebPostResDto newCeleb;
    @Schema(description = "새로운 Brand")
    private NewBrandPostResDto newBrand;

    @Schema(description = "최신 update 시점")
    private LocalDateTime updatedAt;

    public static TempItemResDto of(TempItem tempItem, CelebDto celeb, NewCelebPostResDto newCeleb,
                                    Brand brand, NewBrandPostResDto newBrand, ItemCategoryDto itemCategory,
                                    List<ItemImgResDto> imgList, List<ItemLinkResDto> linkList,
                                    List<Hashtag> hashtagList
    ) {

        return TempItemResDto.builder()
                .id(tempItem.getId())
                .imgList(imgList)
                .celeb(celeb)
                .newCeleb(newCeleb)
                .category(itemCategory)
                .itemName(tempItem.getName())
                .brand(brand)
                .newBrand(newBrand)
                .linkList(linkList)
                .whenDiscovery(tempItem.getWhenDiscovery())
                .whereDiscovery(tempItem.getWhereDiscovery())
                .price(tempItem.getPrice())
                .additionalInfo(tempItem.getAdditionalInfo())
                .hashTagList(hashtagList)
                .infoSource(tempItem.getInfoSource())
                .updatedAt(tempItem.getUpdatedAt())
                .build();
    }

}
