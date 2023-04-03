package com.sluv.server.domain.item.service;

import com.sluv.server.domain.brand.exception.NotFoundBrandException;
import com.sluv.server.domain.brand.repository.BrandRepository;
import com.sluv.server.domain.celeb.exception.NotFoundCelebException;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.item.dto.ItemPostReqDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.entity.ItemLink;
import com.sluv.server.domain.item.entity.hashtag.ItemHashtag;
import com.sluv.server.domain.item.enums.ItemStatus;
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

import java.util.Map;

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

    public void postItem(User user, ItemPostReqDto reqDto) {
       Item newItem = itemRepository.save(Item.builder()
                .user(user)
                .celeb(
                        celebRepository.findById(reqDto.getCelebId())
                                            .orElseThrow(NotFoundCelebException::new)
                        )
                .category(
                        itemCategoryRepository.findById(reqDto.getCategoryId())
                                            .orElseThrow(NotFoundCelebException::new)
                )
                .brand(
                        brandRepository.findById(reqDto.getBrandId())
                                .orElseThrow(NotFoundBrandException::new)
                )
                .name(reqDto.getItemName())
                .whenDiscovery(reqDto.getWhenDiscovery())
                .whereDiscovery(reqDto.getWhereDiscovery())
                .price(reqDto.getPrice())
                .additionalInfo(reqDto.getAdditionalInfo())
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
        reqDto.getInfoSourceList().stream()
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
