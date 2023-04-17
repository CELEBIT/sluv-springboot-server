package com.sluv.server.domain.item.service;

import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.exception.BrandNotFoundException;
import com.sluv.server.domain.brand.repository.BrandRepository;
import com.sluv.server.domain.celeb.dto.CelebDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.item.dto.*;
import com.sluv.server.domain.item.entity.*;
import com.sluv.server.domain.item.entity.hashtag.Hashtag;
import com.sluv.server.domain.item.entity.hashtag.TempItemHashtag;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.domain.item.exception.ItemCategoryNotFoundException;
import com.sluv.server.domain.item.exception.hashtag.NotFoundHashtagException;
import com.sluv.server.domain.item.repository.*;
import com.sluv.server.domain.item.repository.hashtag.HashtagRepository;
import com.sluv.server.domain.item.repository.hashtag.TempItemHashtagRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TempItemService {
    private final TempItemRepository tempItemRepository;

    private final TempItemLinkRepository tempItemLinkRepository;
    private final TempItemImgRepository tempItemImgRepository;
    private final TempItemHashtagRepository tempItemHashtagRepository;



    private final HashtagRepository hashtagRepository;
    private final CelebRepository celebRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final BrandRepository brandRepository;


    public void postTempItem(User user, TempItemPostReqDto reqDto) {
        Celeb celeb = null;
        if(reqDto.getCelebId() != null){
            celeb = celebRepository.findById(reqDto.getCelebId())
                    .orElseThrow(CelebNotFoundException::new);
        }

        Brand brand = null;
        if(reqDto.getBrandId() != null){
            brand = brandRepository.findById(reqDto.getBrandId())
                    .orElseThrow(BrandNotFoundException::new);
        }
        ItemCategory itemCategory = null;
        if(reqDto.getCategoryId() != null){
            itemCategory = itemCategoryRepository.findById(reqDto.getCategoryId())
                    .orElseThrow(ItemCategoryNotFoundException::new);
        }

        Integer price = null;
        if(reqDto.getPrice() != null){
            price = reqDto.getPrice();
        }

        TempItem tempitem = tempItemRepository.save(TempItem
                .builder()
                .user(user)
                .celeb(celeb)
                .newCelebName(reqDto.getNewCelebName())
                .category(itemCategory)
                .brand(brand)
                .newBrandName(reqDto.getNewBrandName())
                .name(reqDto.getItemName())
                .whenDiscovery(reqDto.getWhenDiscovery())
                .whereDiscovery(reqDto.getWhereDiscovery())
                .price(price)
                .additionalInfo(reqDto.getAdditionalInfo())
                .infoSource(reqDto.getInfoSource())
                .itemStatus(ItemStatus.ACTIVE)
                .build()
            );

        // ItemImg 테이블에 추가
        if(reqDto.getImgList() != null) {

            reqDto.getImgList().stream()
                            .map(tempItemImg ->
                                TempItemImg.builder()
                                        .tempItem(tempitem)
                                        .tempItemImgUrl(tempItemImg.getImgUrl())
                                        .representFlag(tempItemImg.getRepresentFlag())
                                        .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
                                        .build()
                            ).forEach(tempItemImgRepository::save);

        }

        // ItemLink 테이블에 추가
        if(reqDto.getLinkList() != null) {
            reqDto.getLinkList().stream()
                            .map(tempItemLink ->
                                    TempItemLink.builder()
                                            .tempItem(tempitem)
                                            .linkName(tempItemLink.getLinkName())
                                            .tempItemLinkUrl(tempItemLink.getItemLinkUrl())
                                            .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
                                            .build()

                            ).forEach(tempItemLinkRepository::save);
        }

        // ItemHashtag 테이블에 추가
        if(reqDto.getHashTagIdList() != null) {
            reqDto.getHashTagIdList().stream().map(hashTag ->

                    TempItemHashtag.builder()
                            .tempItem(tempitem)
                            .hashtag(
                                    hashtagRepository.findById(hashTag)
                                            .orElseThrow(NotFoundHashtagException::new)
                            )
                            .build()

            ).forEach(tempItemHashtagRepository::save);
        }

    }

    public List<TempItemResDto> getTempItemList(User user, Pageable pageable){


        return tempItemRepository.getTempItemList(user, pageable).stream().map(tempItem -> {

            List<ItemImgResDto> tempImgList = tempItemImgRepository.findAllByTempItem(tempItem)
                    .stream().map(tempItemImg -> ItemImgResDto.builder()
                            .imgUrl(tempItemImg.getTempItemImgUrl())
                            .representFlag(tempItemImg.getRepresentFlag())
                            .build()
                    ).collect(Collectors.toList());

            List<Hashtag> tempHashtagList = tempItemHashtagRepository.findAllByTempItem(tempItem)
                    .stream().map(TempItemHashtag::getHashtag).toList();

            List<ItemLinkResDto> tempLinkList = tempItemLinkRepository.findAllByTempItem(tempItem)
                    .stream().map(tempItemLink -> ItemLinkResDto.builder()
                            .linkName(tempItemLink.getLinkName())
                            .itemLinkUrl(tempItemLink.getTempItemLinkUrl())
                            .build()
                    ).collect(Collectors.toList());

                CelebDto celebDto = tempItem.getCeleb() != null ?
                        CelebDto.builder()
                        .id(tempItem.getCeleb().getId())
                        .celebNameKr(tempItem.getCeleb().getCelebNameKr())
                        .celebNameEn(tempItem.getCeleb().getCelebNameEn())
                        .categoryChild(tempItem.getCeleb().getCelebCategory().getName())
                        .categoryParent(tempItem.getCeleb().getCelebCategory().getParent().getName())
                        .parentCelebNameKr(tempItem.getCeleb().getParent() != null ? tempItem.getCeleb().getParent().getCelebNameKr() : null)
                        .parentCelebNameEn(tempItem.getCeleb().getParent() != null ? tempItem.getCeleb().getParent().getCelebNameEn() : null)
                        .build()
                        : null;

                ItemCategoryDto itemCategory = tempItem.getCategory() != null ?
                        ItemCategoryDto.builder()
                                .id(tempItem.getCategory().getId())
                                .name(tempItem.getCategory().getName())
                                .parentName(tempItem.getCategory().getParent().getName())
                                .build()
                        : null;


            return TempItemResDto.builder()
                            .id(tempItem.getId())
                            .imgList(tempImgList)
                            .celeb(celebDto)
                            .whenDiscovery(tempItem.getWhenDiscovery())
                            .whereDiscovery(tempItem.getWhereDiscovery())
                            .category(itemCategory)
                            .itemName(tempItem.getName())
                            .price(tempItem.getPrice())
                            .additionalInfo(tempItem.getAdditionalInfo())
                            .hashTagList(tempHashtagList)
                            .linkList(tempLinkList)
                    .infoSource(tempItem.getInfoSource())
                    .newCelebName(tempItem.getNewCelebName())
                    .newBrandName(tempItem.getNewBrandName())
                    .updatedAt(tempItem.getUpdatedAt())
                    .build();

            }
        ).collect(Collectors.toList());
    }
}
