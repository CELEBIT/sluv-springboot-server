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
import com.sluv.server.domain.search.dto.SearchFilterReqDto;
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
                                    .sortOrder(itemImg.getSortOrder())
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
                                                                .sortOrder(itemImg.getSortOrder())
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
        Integer scrapNum = itemScrapRepository.countByItemId(item.getId());

        // 7. 조회수 TODO : Redis를 사용한 IP:PostId 저장으로 조회수 중복방지 기능
        Long viewNum = item.getViewNum();

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
        List<ItemSimpleResDto> sameCelebItemList = convertItemToItemSameResDto(
                                                                user , itemRepository.findSameCelebItem(item, celebJudge)
                                                );

        // 12. 같은 브랜드 아이템 리스트
        boolean brandJudge = item.getBrand() != null;
        List<ItemSimpleResDto> sameBrandItemList = convertItemToItemSameResDto(
                                                            user, itemRepository.findSameBrandItem(item, brandJudge)
                                                    );

        // 13. 다른 스러버들이 함께 보관한 아이템 리스트
        List<ItemSimpleResDto> sameClosetItemList = convertItemToItemSameResDto(
                                                            user, getClosetItemList(item)
                                                    );


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
                .likeNum(likeNum)
                .likeStatus(likeStatus)
                .scrapNum(scrapNum)
                .viewNum(viewNum)
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
                .otherSluverItemList(sameClosetItemList)
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

        item.decreaseViewNum();
    }

    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        item.changeStatus(ItemStatus.DELETED);
        itemRepository.save(item);
    }

    public PaginationResDto<ItemSimpleResDto> getRecentItem(User user, Pageable pageable) {
        Page<Item> recentItemPage = itemRepository.getRecentItem(user, pageable);

        return convertItemSamePageDto(user, pageable, recentItemPage);


    }

    public PaginationResDto<ItemSimpleResDto> getScrapItem(User user, Pageable pageable) {
        // User, Closet, Item 조인하여 ItemPage 조회
        Page<Item> itemPage = itemRepository.getAllScrapItem(user, pageable);

        return convertItemSamePageDto(user, pageable, itemPage);
    }

    private PaginationResDto<ItemSimpleResDto> convertItemSamePageDto(User user, Pageable pageable, Page<Item> page) {
        // ItemPage에서 ItemSameResDto 생성
        List<ItemSimpleResDto> content = convertItemToItemSameResDto(user, page.getContent());

        return PaginationResDto.<ItemSimpleResDto>builder()
                .page(page.getNumber())
                .hasNext(page.hasNext())
                .content(content)
                .build();
    }

    private List<Item> getClosetItemList(Item item) {
        // 가장 최근에 해당 item을 추가한 상위 20개의 Closet을 검색
        List<Closet> recentAddClosetList = closetRepository.getRecentAddCloset(item);

        // 해당 Closet에 해당하는 아이템들을 최신순으로 정렬후 10개 추출.
        return itemRepository.getSameClosetItems(item, recentAddClosetList);
    }

    private List<ItemSimpleResDto> convertItemToItemSameResDto(User user, List<Item> itemList){
        // User의 모든 Closet 조회
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());

        return itemList.stream()
                .map(item ->{
                    ItemImg itemImg = itemImgRepository.findMainImg(item.getId());
                    Boolean scrapStatus = itemScrapRepository.getItemScrapStatus(item, closetList);
                    return ItemSimpleResDto.builder()
                            .itemId(item.getId())
                            .itemName(item.getName())
                            .brandName(
                                    item.getBrand() != null
                                    ?item.getBrand().getBrandKr()
                                    :item.getNewBrand().getBrandName()
                            )
                            .celebName(
                                    item.getCeleb() != null
                                            ? item.getCeleb().getCelebNameKr()
                                            : item.getNewCeleb().getCelebName()
                            )
                            .imgUrl(itemImg.getItemImgUrl())
                            .scrapStatus(scrapStatus)
                            .build();
                })
                .toList();
    }

    public PaginationResDto<ItemSimpleResDto> getRecommendItem(User user, Pageable pageable) {
        Page<Item> recommendItemPage = itemRepository.getRecommendItemPage(pageable);

        List<ItemSimpleResDto> content =
                convertItemToItemSameResDto(user, recommendItemPage.getContent());



        return PaginationResDto.<ItemSimpleResDto>builder()
                .page(recommendItemPage.getNumber())
                .hasNext(recommendItemPage.hasNext())
                .content(content)
                .build();
    }

    /**
     * 핫한 셀럽들이 선택한 여름나기 아이템 조회
     */
    public PaginationResDto<ItemSimpleResDto> getSummerItem(User user, Pageable pageable, SearchFilterReqDto dto) {
        // itemPage 조회
        Page<Item> itemPage = itemRepository.getCelebSummerItem(pageable, dto);
        // Content 조립
        List<ItemSimpleResDto> content = convertItemToItemSameResDto(user, itemPage.getContent());

        return PaginationResDto.<ItemSimpleResDto>builder()
                .page(itemPage.getNumber())
                .hasNext(itemPage.hasNext())
                .content(content)
                .build();
    }

    /**
     * 지금 당장 구매가능한 아이템 조회
     */
    public PaginationResDto<ItemSimpleResDto> getNowBuyItem(User user, Pageable pageable, SearchFilterReqDto dto) {
        // itemPage 조회
        Page<Item> itemPage = itemRepository.getNowBuyItem(pageable, dto);

        // Content 조립
        List<ItemSimpleResDto> content = convertItemToItemSameResDto(user, itemPage.getContent());

        return PaginationResDto.<ItemSimpleResDto>builder()
                .page(itemPage.getNumber())
                .hasNext(itemPage.hasNext())
                .content(content)
                .build();

    }
}
