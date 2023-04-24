package com.sluv.server.domain.item.service;

import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.brand.enums.NewBrandStatus;
import com.sluv.server.domain.brand.exception.BrandNotFoundException;
import com.sluv.server.domain.brand.repository.BrandRepository;
import com.sluv.server.domain.brand.repository.NewBrandRepository;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.enums.NewCelebStatus;
import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import com.sluv.server.domain.item.dto.ItemPostReqDto;
import com.sluv.server.domain.item.entity.*;
import com.sluv.server.domain.item.entity.hashtag.ItemHashtag;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.domain.item.exception.ItemCategoryNotFoundException;
import com.sluv.server.domain.item.exception.hashtag.HashtagNotFoundException;
import com.sluv.server.domain.item.repository.*;
import com.sluv.server.domain.item.repository.hashtag.HashtagRepository;
import com.sluv.server.domain.item.repository.hashtag.ItemHashtagRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    private final PlaceRankRepository placeRankRepository;

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
        if(reqDto.getNewCelebName() != null){
            // 이미 NewCeleb 테이블에 있다면, DB에 있는 것을 할당
            newCeleb = newCelebRepository.findByCelebName(reqDto.getNewCelebName()).orElse(null);

            if(newCeleb == null){
                // NewCeleb 테이블에 없다면, 저장 후 할당
                newCeleb = newCelebRepository.save(NewCeleb.builder()
                        .celebName(reqDto.getNewCelebName())
                        .newCelebStatus(NewCelebStatus.ACTIVE)
                        .build()
                );
            }
        }

        NewBrand newBrand = null;
        if(reqDto.getNewBrandName() != null){
            // 이미 NewBrand 테이블에 있다면, DB에 있는 것을 할당
            newBrand = newBrandRepository.findByBrandName(reqDto.getNewBrandName()).orElse(null);

            if(newBrand == null){
                // NewBrand 테이블에 없다면, 저장 후 할당
                newBrand = newBrandRepository.save(NewBrand.builder()
                        .brandName(reqDto.getNewBrandName())
                        .newBrandStatus(NewBrandStatus.ACTIVE)
                        .build()
                );
            }

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
                        .map(itemImg->
                            ItemImg.builder()
                                    .item(newItem)
                                    .itemImgUrl(itemImg.getImgUrl())
                                    .representFlag(itemImg.getRepresentFlag())
                                    .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
                                    .build()
                        ).forEach(itemImgRepository::save);



        // ItemLink 테이블에 추가
        reqDto.getLinkList().stream()
                                    .map(itemLink ->
                                            ItemLink.builder()
                                                    .item(newItem)
                                                    .linkName(itemLink.getLinkName())
                                                    .itemLinkUrl(itemLink.getItemLinkUrl())
                                                    .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
                                                    .build()

                                    ).forEach(itemLinkRepository::save);


        // ItemHashtag 테이블에 추가
        reqDto.getHashTagIdList().stream().map(hashTag ->

                    ItemHashtag.builder()
                            .item(newItem)
                            .hashtag(
                                    hashtagRepository.findById(hashTag)
                                            .orElseThrow(HashtagNotFoundException::new)
                            )
                            .build()

                ).forEach(itemHashtagRepository::save);

        // PlaceRank 테이블에 추가
        if(reqDto.getWhereDiscovery() != null) {
            placeRankRepository.save(PlaceRank.builder()
                    .place(reqDto.getWhereDiscovery())
                    .build()
            );
        }

    }
}
