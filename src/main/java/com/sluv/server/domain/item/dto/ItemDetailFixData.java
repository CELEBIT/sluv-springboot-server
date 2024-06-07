package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.brand.dto.BrandSearchResDto;
import com.sluv.server.domain.brand.dto.NewBrandPostResDto;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.celeb.dto.CelebSearchResDto;
import com.sluv.server.domain.celeb.dto.NewCelebPostResDto;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.user.dto.UserInfoDto;
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
    private List<ItemImgResDto> imgList;
    private CelebSearchResDto celeb;
    private NewCelebPostResDto newCeleb;
    private ItemCategoryDto category;
    private String itemName;
    private BrandSearchResDto brand;
    private NewBrandPostResDto newBrand;
    private List<ItemLinkResDto> linkList;
    private UserInfoDto writer;
    private LocalDateTime whenDiscovery;
    private String whereDiscovery;
    private Integer price;
    private String additionalInfo;
    private List<ItemHashtagResponseDto> hashTagList;
    private String infoSource;
    private String color;

    public static ItemDetailFixData of(Item item, CelebSearchResDto celeb, NewCeleb newCeleb,
                                       BrandSearchResDto brand, NewBrand newBrand, ItemCategoryDto itemCategory,
                                       UserInfoDto writerInfo, List<ItemImgResDto> imgList,
                                       List<ItemLinkResDto> linkList,
                                       List<ItemHashtagResponseDto> hashtagList
    ) {

        NewCelebPostResDto newCelebPostResDto = newCeleb == null ? null : NewCelebPostResDto.of(newCeleb);
        NewBrandPostResDto newBrandPostResDto = newBrand == null ? null : NewBrandPostResDto.of(newBrand);

        return ItemDetailFixData.builder()
                .imgList(imgList)
                .celeb(celeb)
                .newCeleb(newCelebPostResDto)
                .category(itemCategory)
                .itemName(item.getName())
                .brand(brand)
                .newBrand(newBrandPostResDto)
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
