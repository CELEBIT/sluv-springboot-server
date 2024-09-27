package com.sluv.api.item.dto;

import com.sluv.api.brand.dto.response.BrandSearchResponse;
import com.sluv.api.brand.dto.response.NewBrandPostResponse;
import com.sluv.api.celeb.dto.response.CelebSearchResponse;
import com.sluv.api.celeb.dto.response.NewCelebPostResponse;
import com.sluv.domain.item.dto.ItemImgDto;
import com.sluv.domain.item.dto.ItemLinkDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.user.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
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
public class ItemDetailResDto {

    /**
     * 아이템 세부 검색 정보 전달
     */


    @Schema(description = "item 이미지 리스트")
    private List<ItemImgDto> imgList;
    @Schema(description = "celeb")
    private CelebSearchResponse celeb;
    @Schema(description = "새로운 Celeb")
    private NewCelebPostResponse newCeleb;
    @Schema(description = "아이템 카테고리")
    private ItemCategoryDto category;
    @Schema(description = "아이템명")
    private String itemName;
    @Schema(description = "브랜드")
    private BrandSearchResponse brand;
    @Schema(description = "새로운 Brand")
    private NewBrandPostResponse newBrand;
    @Schema(description = "좋아요 수")
    private Integer likeNum;
    @Schema(description = "좋아요 여부")
    private Boolean likeStatus;
    @Schema(description = "스크랩 수")
    private Integer scrapNum;
    @Schema(description = "스크랩 여부")
    private Boolean scrapStatus;
    @Schema(description = "조회수")
    private Long viewNum;
    @Schema(description = "item 링크 리스트")
    private List<ItemLinkDto> linkList;
    @Schema(description = "게시글 작성자")
    private UserInfoDto writer;
    @Schema(description = "발견 시간 ex)2021-11-20T09:10:20")
    private LocalDateTime whenDiscovery;
    @Size(max = 100)
    @Schema(description = "발견 장소")
    private String whereDiscovery;
    @Schema(description = "가격")
    private Integer price;
    @Schema(description = "추가정보")
    private String additionalInfo;
    @Schema(description = "해쉬태그 리스트")
    private List<ItemHashtagResponseDto> hashTagList;
    @Schema(description = "추가정보를 발견한 출처")
    private String infoSource;

    @Schema(description = "색")
    private String color;

    @Schema(description = "팔로우 여부")
    private Boolean followStatus;

    @Schema(description = "현재 유저가 작성한 글인지 판단")
    private Boolean hasMine;

    public static ItemDetailResDto of(Item item, CelebSearchResponse celeb, NewCelebPostResponse newCeleb,
                                      BrandSearchResponse brand, NewBrandPostResponse newBrand,
                                      ItemCategoryDto itemCategory,
                                      Integer likeNum, Boolean likeStatus, Integer scrapNum, Boolean scrapStatus,
                                      Long viewNum, UserInfoDto writerInfo, Boolean followStatus, Boolean hasMine,
                                      List<ItemImgDto> imgList, List<ItemLinkDto> linkList,
                                      List<ItemHashtagResponseDto> hashtagList
    ) {

        return ItemDetailResDto.builder()
                .imgList(imgList)
                .celeb(celeb)
                .newCeleb(newCeleb)
                .category(itemCategory)
                .itemName(item.getName())
                .brand(brand)
                .newBrand(newBrand)
                .likeNum(likeNum)
                .likeStatus(likeStatus)
                .scrapNum(scrapNum)
                .scrapStatus(scrapStatus)
                .viewNum(viewNum)
                .linkList(linkList)
                .writer(writerInfo)
                .whenDiscovery(item.getWhenDiscovery())
                .whereDiscovery(item.getWhereDiscovery())
                .price(item.getPrice())
                .additionalInfo(item.getAdditionalInfo())
                .hashTagList(hashtagList)
                .infoSource(item.getInfoSource())
                .followStatus(followStatus)
                .hasMine(hasMine)
                .color(item.getColor())
                .build();
    }


}
