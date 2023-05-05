package com.sluv.server.domain.item.service;

import com.sluv.server.domain.brand.dto.BrandSearchResDto;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.brand.exception.BrandNotFoundException;
import com.sluv.server.domain.brand.exception.NewBrandNotFoundException;
import com.sluv.server.domain.brand.repository.BrandRepository;
import com.sluv.server.domain.brand.repository.NewBrandRepository;
import com.sluv.server.domain.brand.repository.RecentSelectBrandRepository;
import com.sluv.server.domain.celeb.dto.CelebSearchResDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.celeb.exception.NewCelebNotFoundException;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import com.sluv.server.domain.celeb.repository.RecentSelectCelebRepository;
import com.sluv.server.domain.item.dto.*;
import com.sluv.server.domain.item.entity.*;
import com.sluv.server.domain.item.entity.hashtag.ItemHashtag;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.domain.item.exception.ItemCategoryNotFoundException;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.exception.hashtag.HashtagNotFoundException;
import com.sluv.server.domain.item.repository.*;
import com.sluv.server.domain.item.repository.hashtag.HashtagRepository;
import com.sluv.server.domain.item.repository.hashtag.ItemHashtagRepository;
import com.sluv.server.domain.user.dto.UserInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private final UserRepository userRepository;

    private final NewBrandRepository newBrandRepository;
    private final NewCelebRepository newCelebRepository;

    private final PlaceRankRepository placeRankRepository;
    private final RecentSelectCelebRepository recentSearchCelebRepository;
    private final RecentSelectBrandRepository recentSelectBrandRepository;

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
        if(reqDto.getNewCelebId() != null){
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
                .viewNum(0L)
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
        if(reqDto.getLinkList() != null) {
            reqDto.getLinkList().stream()
                    .map(itemLink ->
                            ItemLink.builder()
                                    .item(newItem)
                                    .linkName(itemLink.getLinkName())
                                    .itemLinkUrl(itemLink.getItemLinkUrl())
                                    .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
                                    .build()

                    ).forEach(itemLinkRepository::save);
        }


        // ItemHashtag 테이블에 추가
        if(reqDto.getHashTagIdList() != null) {
            reqDto.getHashTagIdList().stream().map(hashTag ->

                    ItemHashtag.builder()
                            .item(newItem)
                            .hashtag(
                                    hashtagRepository.findById(hashTag)
                                            .orElseThrow(HashtagNotFoundException::new)
                            )
                            .build()

            ).forEach(itemHashtagRepository::save);
        }
    }

    public List<HotPlaceResDto> getTopPlace() {

        return itemRepository.findTopPlace().stream()
                .map(placeName ->
                        HotPlaceResDto.builder()
                                .placeName(placeName)
                                .build()
                ).toList();

    }

    public ItemDetailResDto getItemDetail(Long itemId) {

        // 1. Item 조회
        Item item = itemRepository.findById(itemId)
                                    .orElseThrow(ItemNotFoundException::new);

        item.increaseViewNum();
        itemRepository.save(item);

        // 2. Item 이미지들 조회
        List<ItemImgResDto> imgList = itemImgRepository.findAllByItemId(itemId)
                                                        .stream()
                                                        .map(itemImg -> ItemImgResDto.builder()
                                                                .imgUrl(itemImg.getItemImgUrl())
                                                                .representFlag(itemImg.getRepresentFlag())
                                                                .build()
                                                        ).toList();

        // 3. Item Celeb
        CelebSearchResDto celeb = item.getCeleb() != null ?

                CelebSearchResDto.builder()
                                .id(item.getCeleb().getId())
                                .category(null)
                                .celebNameKr(item.getCeleb().getCelebNameKr())
                                .celebNameEn(item.getCeleb().getCelebNameEn())
                                .build()
                : null;

        String newCeleb = item.getNewCeleb() != null ?
                item.getNewCeleb().getCelebName()
                : null;

        // 4. Item Category
        ItemCategoryDto category = ItemCategoryDto.builder()
                .id(item.getCategory().getId())
                .parentId(
                        item.getCategory().getParent() != null
                            ? item.getCategory().getParent().getId()
                            : null
                )
                .name(item.getCategory().getName())
                .parentName(
                        item.getCategory().getParent() != null
                                ? item.getCategory().getParent().getName()
                                : null
                )
                .build();

        // 4. Brand
        BrandSearchResDto brand = item.getBrand() != null ?
                    BrandSearchResDto.builder()
                        .id(item.getBrand().getId())
                        .brandKr(item.getBrand().getBrandKr())
                        .brandEn(item.getBrand().getBrandEn())
                        .brandImgUrl(item.getBrand().getBrandImgUrl())
                        .build()
                : null;

        String newBrand = item.getNewBrand() != null ?
                item.getNewBrand().getBrandName()
                : null;

        // 5. 좋아요 수


        // 6. 스크랩 수

        // 7. 조회수
//        Long viewNum = item.getViewNum();

        // 8. Item 링크들 조회

        List<ItemLinkResDto> linkList = itemLinkRepository.findByItemId(itemId)
                                                        .stream()
                                                        .map(itemLink -> ItemLinkResDto.builder()
                                                                .linkName(itemLink.getLinkName())
                                                                .itemLinkUrl(itemLink.getItemLinkUrl())
                                                                .build()
                                                        )
                                                        .toList();

        // 9. 작성자 info
        User user = userRepository.findById(item.getUser().getId())
                                                .orElseThrow(UserNotFoundException::new);
        UserInfoDto userInfo = UserInfoDto.builder()
                .id(user.getId())
                .profileImgUrl(user.getProfileImgUrl())
                .nickName(user.getNickname())
                .build();

        // 10. Hashtag
        List<ItemHashtag> itemHashtagId = itemHashtagRepository.findAllByItemId(itemId);
        List<HashtagResponseDto> hashtagList = itemHashtagId.stream()
            .parallel()
            .map(itemHashtag ->
            HashtagResponseDto.builder()
                    .hashtagId(itemHashtag.getHashtag().getId())
                    .hashtagContent(itemHashtag.getHashtag().getContent())
                    .count(null)
                    .build()
        ).toList();


        // 11. 같은 셀럽 아이템 리스트
        boolean celebJudge = item.getCeleb() != null;
        String celebName = celebJudge ? item.getCeleb().getCelebNameKr() : item.getNewCeleb().getCelebName();

        List<ItemSameResDto> sameCelebItemList = itemRepository.findSameCelebItem(item, celebJudge)
                        .stream().map(celebItem -> {
                           ItemImg itemImg = itemImgRepository.findMainImg(celebItem.getId());
                            return ItemSameResDto.builder()
                                    .itemId(celebItem.getId())
                                    .itemName(celebItem.getName())
                                    .brandName(
                                            celebItem.getBrand() != null
                                            ?celebItem.getBrand().getBrandKr()
                                            : celebItem.getNewBrand().getBrandName()
                                    )
                                    .celebName(celebName)
                                    .imgUrl(itemImg.getItemImgUrl())
                                    .build();
                        }).toList();


        // 12. 같은 브랜드 아이템 리스트
        boolean brandJudge = item.getBrand() != null;
        String brandName = brandJudge ? item.getBrand().getBrandKr() : item.getNewBrand().getBrandName();

        List<ItemSameResDto> sameBrandItemList = itemRepository.findSameBrandItem(item, brandJudge)
                        .stream()
                        .map(brandItem ->{
                            ItemImg itemImg = itemImgRepository.findMainImg(brandItem.getId());
                            return ItemSameResDto.builder()
                                    .itemId(brandItem.getId())
                                    .itemName(brandItem.getName())
                                    .brandName(brandName)
                                    .celebName(
                                            brandItem.getCeleb() != null
                                            ? brandItem.getCeleb().getCelebNameKr()
                                            : brandItem.getNewCeleb().getCelebName()
                                    )
                                    .imgUrl(itemImg.getItemImgUrl())
                                    .build();
                        })
                        .toList();
        // 13. 다른 스러버들이 함께 보관한 아이템 리스트

        // Dto 조립
        return ItemDetailResDto.builder()
                .imgList(imgList)
                .celeb(celeb)
                .newCelebName(newCeleb)
                .category(category)
                .itemName(item.getName())
                .brand(brand)
                .newBrandName(newBrand)
                .likeNum(null) // like 완성 후 변경 예정
                .scrapNum(null) // closet 완성 후 변경 예정
                .viewNum(item.getViewNum())
                .linkList(linkList)
                .user(userInfo)
                .whenDiscovery(item.getWhenDiscovery())
                .whereDiscovery(item.getWhereDiscovery())
                .price(item.getPrice())
                .additionalInfo(item.getAdditionalInfo())
                .hashTagList(hashtagList)
                .infoSource(item.getInfoSource())
                .sameCelebItemList(sameCelebItemList)
                .sameBrandItemList(sameBrandItemList)
//                .sameClosetItemList(sameClosetItemList)
                .color(item.getColor())
                .build();
    }
}
