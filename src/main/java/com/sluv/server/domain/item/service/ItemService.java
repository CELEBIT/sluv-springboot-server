package com.sluv.server.domain.item.service;

import com.sluv.server.domain.brand.dto.BrandSearchResDto;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.brand.exception.BrandNotFoundException;
import com.sluv.server.domain.brand.exception.NewBrandNotFoundException;
import com.sluv.server.domain.brand.repository.BrandRepository;
import com.sluv.server.domain.brand.repository.NewBrandRepository;
import com.sluv.server.domain.celeb.dto.CelebSearchResDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.celeb.exception.NewCelebNotFoundException;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
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
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final ItemLikeRepository itemLikeRepository;
    private final FollowRepository followRepository;
    private final ClosetRepository closetRepository;
    private final ItemScrapRepository itemScrapRepository;

    @Transactional
    public ItemPostResDto postItem(User user, ItemPostReqDto reqDto) {

        // 추가될 Celeb 확인
        Celeb celeb = null;
        if(reqDto.getCelebId() != null){
            celeb = celebRepository.findById(reqDto.getCelebId())
                    .orElseThrow(CelebNotFoundException::new);
        }

        // 추가될 Brand 확인
        Brand brand = null;
        if(reqDto.getBrandId() != null){
            brand = brandRepository.findById(reqDto.getBrandId())
                    .orElseThrow(BrandNotFoundException::new);
        }

        // 추가될 NewCeleb 확인
        NewCeleb newCeleb = null;
        if(reqDto.getNewCelebId() != null){
            newCeleb = newCelebRepository.findById(reqDto.getNewCelebId())
                    .orElseThrow(NewCelebNotFoundException::new);
        }

        // 추가될 NewBrand 확인
        NewBrand newBrand = null;
        if(reqDto.getNewCelebId() != null){
            newBrand = newBrandRepository.findById(reqDto.getNewBrandId())
                    .orElseThrow(NewBrandNotFoundException::new);
        }

        // 추가될 Category 확인
        ItemCategory itemCategory = itemCategoryRepository.findById(reqDto.getCategoryId())
                .orElseThrow(ItemCategoryNotFoundException::new);

        // Item을 등록인지 수정인지 판단.
        Item item = reqDto.getId() != null ? itemRepository.findById(reqDto.getId()).orElseThrow(ItemNotFoundException::new)
                :null;

        // Item id를 제외한 나머지 부분 Builder로 조립.
        Item.ItemBuilder itemBuilder = Item.builder()
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
                .viewNum(0L);

        // Item 수정이라면, 기존의 Item의 Id를 추가.
        if(item != null){
            itemBuilder.id(item.getId());
        }

        // 완성된 Item save
        Item newItem = itemRepository.save(
                itemBuilder.build()
        );

        // 기존의 Img, Link, Hashtag가 있다면 모두 삭제
        itemImgRepository.deleteAllByItemId(newItem.getId());
        itemLinkRepository.deleteAllByItemId(newItem.getId());
        itemHashtagRepository.deleteAllByItemId(newItem.getId());

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



        return ItemPostResDto.builder()
                .itemId(newItem.getId())
                .build();
    }

    public List<HotPlaceResDto> getTopPlace() {

        return itemRepository.findTopPlace().stream()
                .map(placeName ->
                        HotPlaceResDto.builder()
                                .placeName(placeName)
                                .build()
                ).toList();

    }

    public ItemDetailResDto getItemDetail(User user, Long itemId) {

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
                                .celebParentNameKr(
                                        item.getCeleb().getParent() != null
                                        ? item.getCeleb().getParent().getCelebNameKr()
                                        :null
                                )
                                .celebChildNameKr(item.getCeleb().getCelebNameKr())
                                .celebTotalNameKr(
                                        item.getCeleb().getParent() != null
                                        ? item.getCeleb().getParent().getCelebNameKr() + " " + item.getCeleb().getCelebNameKr()
                                        : item.getCeleb().getCelebNameKr()
                                )
                                .celebTotalNameEn(
                                        item.getCeleb().getParent() != null
                                        ? item.getCeleb().getParent().getCelebNameEn() + " " + item.getCeleb().getCelebNameEn()
                                        : item.getCeleb().getCelebNameEn()
                                )
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
        Integer likeNum = itemLikeRepository.countByItemId(item.getId());

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
        User writer = userRepository.findById(item.getUser().getId())
                                                .orElseThrow(UserNotFoundException::new);
        UserInfoDto writerInfo = UserInfoDto.builder()
                .id(writer.getId())
                .profileImgUrl(writer.getProfileImgUrl())
                .nickName(writer.getNickname())
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


        // 14. 좋아요 여부
        boolean likeStatus = itemLikeRepository.existsByUserIdAndItemId(user.getId(), itemId);

        // 15. 팔로우 여부
        boolean followStatus = followRepository.getFollowStatus(user, writer);

        // Dto 조립
        return ItemDetailResDto.builder()
                .imgList(imgList)
                .celeb(celeb)
                .newCelebName(newCeleb)
                .category(category)
                .itemName(item.getName())
                .brand(brand)
                .newBrandName(newBrand)
                .likeNum(likeNum) // like 완성 후 변경 예정
                .likeStatus(likeStatus)
                .scrapNum(null) // closet 완성 후 변경 예정
                .viewNum(item.getViewNum())
                .linkList(linkList)
                .writer(writerInfo)
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
                .followStatus(followStatus)
                .hasMine(item.getUser().getId().equals(user.getId()))
                .build();
    }

    @Transactional
    public void postItemLike(User user, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        boolean likeExist = itemLikeRepository.existsByUserIdAndItemId(user.getId(), itemId);
        if(!likeExist){
            itemLikeRepository.save(
                    ItemLike.builder()
                            .item(item)
                            .user(user)
                            .build()
            );
        }else{
            itemLikeRepository.deleteByUserIdAndItemId(user.getId(), itemId);
        }
    }

    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        item.changeStatus(ItemStatus.DELETED);
        itemRepository.save(item);
    }

    public PaginationResDto<ItemSameResDto> getRecentItem(User user, Pageable pageable) {
        Page<Item> recentItemPage = itemRepository.getRecentItem(user, pageable);
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());

        List<ItemSameResDto> itemList = recentItemPage.getContent()
                .stream()
                .map(item -> {
                    ItemImg mainImg = itemImgRepository.findMainImg(item.getId());
                    List<Boolean> scrapCheckList = closetList.stream()
                                        .map(closet ->
                                                itemScrapRepository.existsByClosetIdAndItemId(closet.getId(),
                                                                                            item.getId()
                                                )
                                        ).toList();
                    return ItemSameResDto.builder()
                                    .itemId(item.getId())
                                    .imgUrl(mainImg.getItemImgUrl())
                                    .brandName(
                                            item.getBrand() != null
                                            ?item.getBrand().getBrandKr()
                                            :item.getNewBrand().getBrandName()
                                    )
                                    .itemName(item.getName())
                                    .celebName(
                                            item.getCeleb() != null
                                            ?item.getCeleb().getCelebNameKr()
                                            :item.getNewCeleb().getCelebName()
                                    )
                                    .scrapStatus(scrapCheckList.contains(true))
                                    .build();
                        }
                ).toList();


        return PaginationResDto.<ItemSameResDto>builder()
                        .page(recentItemPage.getNumber())
                        .hasNext(recentItemPage.hasNext())
                        .content(itemList)
                .build();


    }
}
