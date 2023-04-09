package com.sluv.server.domain.item.service;

import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.brand.exception.BrandNotFoundException;
import com.sluv.server.domain.brand.exception.NewBrandNotFoundException;
import com.sluv.server.domain.brand.repository.BrandRepository;
import com.sluv.server.domain.brand.repository.NewBrandRepository;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.celeb.exception.NewCelebNotFoundException;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import com.sluv.server.domain.item.dto.ItemPostReqDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemCategory;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.entity.ItemLink;
import com.sluv.server.domain.item.entity.hashtag.ItemHashtag;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.domain.item.exception.ItemCategoryNotFoundException;
import com.sluv.server.domain.item.exception.hashtag.NotFoundHashtagException;
import com.sluv.server.domain.item.repository.ItemCategoryRepository;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemLinkRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.hashtag.HashtagRepository;
import com.sluv.server.domain.item.repository.hashtag.ItemHashtagRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemLinkRepository itemLinkRepository;
    private final ItemHashtagRepository itemHashtagRepository;
    private final HashtagRepository hashtagRepository;
    private final CelebRepository celebRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final BrandRepository brandRepository;

    private final NewBrandRepository newBrandRepository;
    private final NewCelebRepository newCelebRepository;

    public void postItem(User user, ItemPostReqDto reqDto) {
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

        NewCeleb newCeleb = null;
        if(reqDto.getNewCelebId() != null){
            newCeleb = newCelebRepository.findById(reqDto.getNewCelebId())
                    .orElseThrow(NewCelebNotFoundException::new);
        }

        NewBrand newBrand = null;
        if(reqDto.getNewBrandId() != null){
            newBrand = newBrandRepository.findById(reqDto.getNewBrandId())
                    .orElseThrow(NewBrandNotFoundException::new);
        }

        ItemCategory itemCategory = itemCategoryRepository.findById(reqDto.getCategoryId())
                .orElseThrow(ItemCategoryNotFoundException::new);


        Item newItem = itemRepository.save(Item.builder()
                .user(user)
                .celeb(celeb)
                .category(itemCategory)
                .brand(brand)
                .newBrand(newBrand)
                .newCeleb(newCeleb)
                .name(reqDto.getItemName())
                .whenDiscovery(reqDto.getWhenDiscovery())
                .whereDiscovery(reqDto.getWhereDiscovery())
                .price(reqDto.getPrice())
                .additionalInfo(reqDto.getAdditionalInfo())
                .infoSource(reqDto.getInfoSource())
                .itemStatus(ItemStatus.ACTIVE)
                .build()
        );

        // ItemImg 테이블에 추가
        reqDto.getImgList().stream()
                .map(img -> img.entrySet().stream()
                        .map(entry -> {
                            boolean flag = entry.getKey() == 1;
                            return ItemImg.builder()
                                    .item(newItem)
                                    .itemImgUrl(entry.getValue())
                                    .representFlag(flag)
                                    .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
                                    .build();
                        })
                        .toList()
                )
                .forEach(itemImgRepository::saveAll);

        // ItemLink 테이블에 추가
        reqDto.getLinkList().stream()
                .map(link -> link.entrySet().stream()
                                                .map(entry ->
                                                        ItemLink.builder()
                                                                .item(newItem)
                                                                .linkName(entry.getKey())
                                                                .itemLinkUrl(entry.getValue())
                                                                .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
                                                                .build()

                                                ).toList()
                ).forEach(itemLinkRepository::saveAll);


        // ItemHashtag 테이블에 추가
        reqDto.getHashTagIdList().stream().map(hashTag ->

                    ItemHashtag.builder()
                            .item(newItem)
                            .hashtag(
                                    hashtagRepository.findById(hashTag)
                                            .orElseThrow(NotFoundHashtagException::new)
                            )
                            .build()

                ).forEach(itemHashtagRepository::save);


    }
}
