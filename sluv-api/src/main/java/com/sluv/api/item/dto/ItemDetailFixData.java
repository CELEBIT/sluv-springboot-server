package com.sluv.api.item.dto;

import com.sluv.api.brand.dto.response.BrandSearchResponse;
import com.sluv.api.brand.dto.response.NewBrandPostResponse;
import com.sluv.api.celeb.dto.response.CelebSearchResponse;
import com.sluv.api.celeb.dto.response.NewCelebPostResponse;
import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.item.dto.ItemImgDto;
import com.sluv.domain.item.dto.ItemLinkDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.user.dto.UserInfoDto;
import java.io.Serializable;
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
public class ItemDetailFixData implements Serializable {
    private List<ItemImgDto> imgList;
    private CelebSearchResponse celeb;
    private NewCelebPostResponse newCeleb;
    private ItemCategoryDto category;
    private String itemName;
    private BrandSearchResponse brand;
    private NewBrandPostResponse newBrand;
    private List<ItemLinkDto> linkList;
    private UserInfoDto writer;
    private LocalDateTime whenDiscovery;
    private String whereDiscovery;
    private Integer price;
    private String additionalInfo;
    private List<ItemHashtagResponseDto> hashTagList;
    private String infoSource;
    private String color;

    public static ItemDetailFixData of(Item item, CelebSearchResponse celeb, NewCeleb newCeleb,
                                       BrandSearchResponse brand, NewBrand newBrand, ItemCategoryDto itemCategory,
                                       UserInfoDto writerInfo, List<ItemImgDto> imgList,
                                       List<ItemLinkDto> linkList,
                                       List<ItemHashtagResponseDto> hashtagList
    ) {

        NewCelebPostResponse newCelebPostResponse = newCeleb == null ? null : NewCelebPostResponse.of(newCeleb);
        NewBrandPostResponse newBrandPostResponse = newBrand == null ? null : NewBrandPostResponse.of(newBrand);

        return ItemDetailFixData.builder()
                .imgList(imgList)
                .celeb(celeb)
                .newCeleb(newCelebPostResponse)
                .category(itemCategory)
                .itemName(item.getName())
                .brand(brand)
                .newBrand(newBrandPostResponse)
                .linkList(linkList)
                .writer(writerInfo)
                .whenDiscovery(item.getWhenDiscovery())
                .whereDiscovery(item.getWhereDiscovery())
                .price(item.getPrice())
                .additionalInfo(item.getAdditionalInfo())
                .hashTagList(hashtagList)
                .infoSource(item.getInfoSource())
                .color(item.getColor())
                .build();
    }
}
