package com.sluv.api.item.service;


import com.sluv.api.brand.dto.response.BrandSearchResponse;
import com.sluv.api.celeb.dto.response.CelebSearchResponse;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.item.dto.*;
import com.sluv.api.item.helper.ItemHelper;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.brand.service.BrandDomainService;
import com.sluv.domain.brand.service.NewBrandDomainService;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.service.CelebDomainService;
import com.sluv.domain.celeb.service.NewCelebDomainService;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.service.ClosetDomainService;
import com.sluv.domain.item.dto.ItemImgDto;
import com.sluv.domain.item.dto.ItemLinkDto;
import com.sluv.domain.item.dto.ItemSaveDto;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemCategory;
import com.sluv.domain.item.entity.hashtag.Hashtag;
import com.sluv.domain.item.enums.ItemStatus;
import com.sluv.domain.item.exception.ItemNotActiveException;
import com.sluv.domain.item.service.*;
import com.sluv.domain.item.service.hashtag.HashtagDomainService;
import com.sluv.domain.search.dto.SearchFilterReqDto;
import com.sluv.domain.user.dto.UserInfoDto;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.FollowDomainService;
import com.sluv.domain.user.service.UserBlockDomainService;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.ai.AiModelService;
import com.sluv.infra.alarm.service.ItemAlarmService;
import com.sluv.infra.cache.CacheService;
import com.sluv.infra.counter.view.ViewCounter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.sluv.domain.item.enums.ItemStatus.ACTIVE;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {
    private final ItemDomainService itemDomainService;
    private final ItemImgDomainService itemImgDomainService;
    private final ItemLinkDomainService itemLinkDomainService;
    private final ItemHashtagDomainService itemHashtagDomainService;
    private final HashtagDomainService hashtagDomainService;
    private final CelebDomainService celebDomainService;
    private final ItemCategoryDomainService itemCategoryDomainService;
    private final BrandDomainService brandDomainService;
    private final UserDomainService userDomainService;
    private final UserBlockDomainService userBlockDomainService;
    private final RecentItemDomainService recentItemDomainService;

    private final NewBrandDomainService newBrandDomainService;
    private final NewCelebDomainService newCelebDomainService;

    private final ItemLikeDomainService itemLikeDomainService;
    private final FollowDomainService followDomainService;
    private final ClosetDomainService closetDomainService;
    private final ItemScrapDomainService itemScrapDomainService;

    private final AiModelService aiModelService;
    private final CacheService<ItemDetailFixData> cacheService;
    private final ViewCounter viewCounter;
    private final ItemAlarmService itemAlarmService;
    private final ItemHelper itemHelper;

    private final String ITEM_KEY_PREFIX = "item:";

    @Transactional
    public ItemPostResDto postItem(Long userId, ItemPostReqDto reqDto) {
        User user = userDomainService.findById(userId);

        if (reqDto.getId() != null) {
            cacheService.deleteByKey(ITEM_KEY_PREFIX + reqDto.getId());
        }

        // 추가될 Celeb 확인
        Celeb celeb = null;
        if (reqDto.getCelebId() != null) {
            celeb = celebDomainService.findById(reqDto.getCelebId());
        }

        // 추가될 Brand 확인
        Brand brand = null;
        if (reqDto.getBrandId() != null) {
            brand = brandDomainService.findById(reqDto.getBrandId());
        }

        // 추가될 NewCeleb 확인
        NewCeleb newCeleb = null;
        if (reqDto.getNewCelebId() != null) {
            newCeleb = newCelebDomainService.findById(reqDto.getNewCelebId());
        }

        // 추가될 NewBrand 확인
        NewBrand newBrand = null;
        if (reqDto.getNewBrandId() != null) {
            newBrand = newBrandDomainService.findById(reqDto.getNewBrandId());
        }

        // 추가될 Category 확인
        ItemCategory itemCategory = itemCategoryDomainService.findById(reqDto.getCategoryId());

        // Item을 등록인지 수정인지 판단.
        Item item =
                reqDto.getId() != null ? itemDomainService.findById(reqDto.getId())
                        : null;

        Item postItem;
        ItemSaveDto itemSaveDto = itemHelper.convertItemPostReqDtoToItemSaveDto(reqDto);
        // Item 수정이라면, 기존의 Item의 Id를 추가.
        if (item != null) {
            postItem = Item.toEntity(item.getId(), user, celeb, newCeleb, itemCategory, brand, newBrand, itemSaveDto,
                    item.getViewNum());
        } else {
            // Item 생성
            postItem = Item.toEntity(user, celeb, newCeleb, itemCategory, brand, newBrand, itemSaveDto);
        }

        // 완성된 Item save
        Item newItem = itemDomainService.saveItem(postItem);

        // 기존의 Img, Link, Hashtag가 있다면 모두 삭제
        itemImgDomainService.deleteAllByItemId(newItem.getId());
        itemLinkDomainService.deleteAllByItemId(newItem.getId());
        itemHashtagDomainService.deleteAllByItemId(newItem.getId());

        // ItemImg 테이블에 추가
//        reqDto.getImgList().stream().map(itemImg -> ItemImg.toEntity(newItem, itemImg))
//                .forEach(itemImgDomainService::saveItemImg);
        reqDto.getImgList().forEach(itemImg -> itemImgDomainService.saveItemImg(newItem, itemImg));

        // ItemLink 테이블에 추가
        if (reqDto.getLinkList() != null) {
            reqDto.getLinkList().forEach(itemLink -> itemLinkDomainService.saveItemLink(newItem, itemLink));
        }

        // ItemHashtag 테이블에 추가
        if (reqDto.getHashTagIdList() != null) {
            reqDto.getHashTagIdList().forEach(hashTag -> {
                Hashtag hashtag = hashtagDomainService.findById(hashTag);
                itemHashtagDomainService.saveItemHashtag(newItem, hashtag);
            });
        }

        aiModelService.getItemColor(newItem);

        if (reqDto.getId() == null) { // 새 아이템 등록 시
            itemAlarmService.sendAlarmAboutFollowItem(user.getId(), newItem.getId());
        }

        return ItemPostResDto.of(newItem.getId());
    }

    @Transactional(readOnly = true)
    public List<HotPlaceResDto> getTopPlace() {

        return itemDomainService.findTopPlace().stream()
                .map(HotPlaceResDto::of).toList();
    }

    @Transactional
    public ItemDetailResDto getItemDetail(Long userId, Long itemId) {

        User user = userDomainService.findByIdOrNull(userId);

        // 1. Item 조회
        Item item = itemDomainService.findById(itemId);

        if (item.getItemStatus() != ACTIVE) {
            log.error("ItemId: {}'s status: {}", itemId, item.getItemStatus());
            throw new ItemNotActiveException();
        }

        // 2. Item Detail 고정 데이터 조회 -> Cache Aside
        ItemDetailFixData fixData = cacheService.findByKey(ITEM_KEY_PREFIX + itemId);
        if (fixData == null) {
            fixData = getItemDetailFixData(item);
            cacheService.saveWithKey(ITEM_KEY_PREFIX + itemId, fixData);
        }

        // 3. 좋아요 수
        Integer likeNum = itemLikeDomainService.countByItemId(item.getId());

        // 4. 스크랩 수
        Integer scrapNum = itemScrapDomainService.countByItemId(item.getId());

        List<Closet> closetList = new ArrayList<>();

        if (user != null) {
            // 5. 최근 본 아이템 등록
            recentItemDomainService.saveRecentItem(item, user);

            // 6. 조회수
            increaseViewNum(user.getId(), item);

            // 7. 스크랩 여부
            closetList = closetDomainService.findAllByUserId(user.getId());
        }

        boolean scrapStatus = itemScrapDomainService.getItemScrapStatus(item, closetList);

        // 8. 좋아요 여부
        boolean likeStatus = user != null && itemLikeDomainService.existsByUserIdAndItemId(user.getId(), itemId);

        // 9. 팔로우 여부
        boolean followStatus = followDomainService.getFollowStatus(user, fixData.getWriter().getId());

        // 10. 본인의 게시글 여부
        boolean hasMine = user != null && item.getUser().getId().equals(user.getId());

        // Dto 조립
        return ItemDetailResDto.of(
                item,
                fixData.getCeleb(),
                fixData.getNewCeleb(),
                fixData.getBrand(),
                fixData.getNewBrand(),
                fixData.getCategory(),
                likeNum,
                likeStatus,
                scrapNum,
                scrapStatus,
                item.getViewNum(),
                fixData.getWriter(),
                followStatus,
                hasMine,
                fixData.getImgList(),
                fixData.getLinkList(),
                fixData.getHashTagList()
        );
    }

    private void increaseViewNum(Long userId, Item item) {
        boolean isExist = viewCounter.existUserViewItemId(userId, item.getId());
        if (!isExist) {
            viewCounter.saveUserViewItemId(userId, item.getId());
            item.increaseViewNum();
        }
    }

    @Transactional
    public ItemDetailFixData getItemDetailFixData(Item item) {
        Long itemId = item.getId();

        // 1. Item Category
        ItemCategoryDto category = ItemCategoryDto.of(item.getCategory());

        // 2. Item Celeb
        CelebSearchResponse celeb = item.getCeleb() != null
                ? CelebSearchResponse.of(item.getCeleb())
                : null;

        // 3. Brand
        BrandSearchResponse brand = item.getBrand() != null ?
                BrandSearchResponse.of(item.getBrand())
                : null;

        // 4. 작성자 info
        User writer = userDomainService.findByIdOrNull(item.getUser().getId());
        UserInfoDto writerInfo = UserInfoDto.of(writer);

        // 5. Item 이미지들 조회
        List<ItemImgDto> imgList = itemImgDomainService.findAllByItemId(itemId)
                .stream()
                .map(ItemImgDto::of).toList();

        // 6. Item 링크들 조회
        List<ItemLinkDto> linkList = itemLinkDomainService.findAllByItemId(itemId)
                .stream()
                .map(ItemLinkDto::of).toList();

        // 7. Hashtag
        List<ItemHashtagResponseDto> itemHashtags = itemHashtagDomainService.findAllByItemId(itemId)
                .stream()
                .map(itemHashtag ->
                        ItemHashtagResponseDto.of(itemHashtag.getHashtag())
                ).toList();

        return ItemDetailFixData.of(item, celeb, item.getNewCeleb(), brand, item.getNewBrand(), category, writerInfo,
                imgList, linkList, itemHashtags);
    }

    @Transactional
    public void postItemLike(Long userId, Long itemId) {
        User user = userDomainService.findById(userId);
        Item item = itemDomainService.findById(itemId);

        boolean likeExist = itemLikeDomainService.existsByUserIdAndItemId(user.getId(), itemId);
        if (!likeExist) {
            itemLikeDomainService.saveItemLike(item, user);
            itemAlarmService.sendAlarmAboutItemLike(user.getId(), item.getId());
        } else {
            itemLikeDomainService.deleteByUserIdAndItemId(user.getId(), itemId);
        }

    }

    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemDomainService.findById(itemId);

        item.changeStatus(ItemStatus.DELETED);
        itemDomainService.saveItem(item);
        cacheService.deleteByKey(ITEM_KEY_PREFIX + itemId);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<ItemSimpleDto> getRecentItem(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);
        Page<Item> recentItemPage = itemDomainService.getRecentItem(user, pageable);
        List<ItemSimpleDto> itemSimpleDtos = itemDomainService.getItemSimpleDto(user,
                recentItemPage.getContent());
        return PaginationResponse.create(recentItemPage, itemSimpleDtos);

    }

    @Transactional(readOnly = true)
    public PaginationResponse<ItemSimpleDto> getScrapItem(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);
        // User, Closet, Item 조인하여 ItemPage 조회
        Page<Item> itemPage = itemDomainService.getAllScrapItem(user, pageable);
        List<ItemSimpleDto> itemSimpleDtos = itemDomainService.getItemSimpleDto(user, itemPage.getContent());
        return PaginationResponse.create(itemPage, itemSimpleDtos);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<ItemSimpleDto> getRecommendItem(Long userId, Pageable pageable) {
        Page<Item> recommendItemPage = itemDomainService.getRecommendItemPage(pageable);
        User user = userDomainService.findByIdOrNull(userId);

        List<ItemSimpleDto> content =
                itemDomainService.getItemSimpleDto(user, recommendItemPage.getContent());

        return PaginationResponse.create(recommendItemPage, content);
    }

    /**
     * 핫한 셀럽들이 선택한 여름나기 아이템 조회
     */
    @Transactional(readOnly = true)
    public PaginationResponse<ItemSimpleDto> getSummerItem(Long userId, Pageable pageable, SearchFilterReqDto dto) {
        // itemPage 조회
        Page<Item> itemPage = itemDomainService.getCelebSummerItem(pageable, dto);
        User user = userDomainService.findByIdOrNull(userId);
        // Content 조립
        List<ItemSimpleDto> content = itemDomainService.getItemSimpleDto(user, itemPage.getContent());

        return PaginationResponse.create(itemPage, content);
    }

    /**
     * 지금 당장 구매가능한 아이템 조회
     */
    @Transactional(readOnly = true)
    public PaginationResponse<ItemSimpleDto> getNowBuyItem(Long userId, Pageable pageable, SearchFilterReqDto dto) {
        User user = userDomainService.findByIdOrNull(userId);
        Page<Item> itemPage = itemDomainService.getNowBuyItem(pageable, dto);
        List<ItemSimpleDto> content = itemDomainService.getItemSimpleDto(user, itemPage.getContent());
        return PaginationResponse.create(itemPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<ItemSimpleDto> getNewItem(Long userId, Pageable pageable) {
        User user = userDomainService.findByIdOrNull(userId);
        List<Long> blockUserIds = new ArrayList<>();
        if (userId != null) {
            blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                    .map(userBlock -> userBlock.getBlockedUser().getId())
                    .toList();
        }

        // itemPage 조회
        Page<Item> itemPage = itemDomainService.getNewItem(blockUserIds, pageable);

        // Content 조립
        List<ItemSimpleDto> content = itemDomainService.getItemSimpleDto(user, itemPage.getContent());

        return PaginationResponse.create(itemPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<ItemSimpleDto> getLuxuryItem(Long userId, Pageable pageable, SearchFilterReqDto dto) {
        User user = userDomainService.findByIdOrNull(userId);
        List<Long> blockUserIds = new ArrayList<>();
        if (userId != null) {
            blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                    .map(userBlock -> userBlock.getBlockedUser().getId())
                    .toList();
        }

        Page<Item> itemPage = itemDomainService.getLuxuryItem(blockUserIds, pageable, dto);
        List<ItemSimpleDto> content = itemDomainService.getItemSimpleDto(user, itemPage.getContent());
        return PaginationResponse.create(itemPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<ItemSimpleDto> getEfficientItem(Long userId, Pageable pageable,
                                                              SearchFilterReqDto filterReqDto) {
        User user = userDomainService.findByIdOrNull(userId);
        List<Long> blockUserIds = new ArrayList<>();
        if (userId != null) {
            blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                    .map(userBlock -> userBlock.getBlockedUser().getId())
                    .toList();
        }

        Page<Item> itemPage = itemDomainService.getEfficientItem(blockUserIds, pageable, filterReqDto);
        List<ItemSimpleDto> content = itemDomainService.getItemSimpleDto(user, itemPage.getContent());
        return PaginationResponse.create(itemPage, content);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleDto> getWeekHotItem(Long userId) {
        User user = userDomainService.findByIdOrNull(userId);
        List<Item> itemPage = itemDomainService.getWeekHotItem();
        return itemDomainService.getItemSimpleDto(user, itemPage);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleDto> getDayHotItem(Long userId) {
        User user = userDomainService.findByIdOrNull(userId);
        List<Item> itemPage = itemDomainService.getDayHotItem();
        return itemDomainService.getItemSimpleDto(user, itemPage);
    }

    /**
     * 요즘 핫한 셀럽의 Item 조회
     */
    @Transactional(readOnly = true)
    public PaginationResponse<ItemSimpleDto> getHotCelebItem(Long userId, Pageable pageable, SearchFilterReqDto dto) {
        User user = userDomainService.findByIdOrNull(userId);
        Long celebId = 510L;

        Page<Item> itemPage = itemDomainService.getHotCelebItem(celebId, pageable, dto);

        List<ItemSimpleDto> content = itemDomainService.getItemSimpleDto(user, itemPage.getContent());

        return PaginationResponse.create(itemPage, content);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleDto> getCurationItem(Long userId) {
        User user = userDomainService.findByIdOrNull(userId);
        List<Long> blockUserIds = new ArrayList<>();

        List<Celeb> interestedCeleb;
        if (user != null) {
            interestedCeleb = celebDomainService.findInterestedCeleb(user);
            blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                    .map(userBlock -> userBlock.getBlockedUser().getId())
                    .toList();
        } else {
            interestedCeleb = celebDomainService.findTop10Celeb();
        }
        List<Item> itemList = itemDomainService.getCurationItem(user, interestedCeleb, blockUserIds);

        return itemDomainService.getItemSimpleDto(user, itemList);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleDto> getHowAboutItem(Long userId) {
        User user = userDomainService.findByIdOrNull(userId);
        List<Long> blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                .map(userBlock -> userBlock.getBlockedUser().getId())
                .toList();

        List<Item> items = itemDomainService.getAllByItemStatus(blockUserIds, ACTIVE);
        Collections.shuffle(items, new Random());
        return itemDomainService.getItemSimpleDto(user, items.subList(0, 4));
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleDto> getSameCelebItems(Long userId, Long itemId) {
        User user = userDomainService.findByIdOrNull(userId);
        Item item = itemDomainService.getItemByIdWithCelebAndBrand(itemId);
        List<Item> sameCelebItems;
        if (item.getCeleb() != null) {
            sameCelebItems = itemDomainService.findSameCelebItem(itemId, item.getCeleb().getId(), true);
        } else {
            sameCelebItems = itemDomainService.findSameCelebItem(itemId, item.getNewCeleb().getId(), false);
        }

        return itemDomainService.getItemSimpleDto(user, sameCelebItems);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleDto> getSameBrandItem(Long userId, Long itemId) {
        User user = userDomainService.findByIdOrNull(userId);
        Item item = itemDomainService.getItemByIdWithCelebAndBrand(itemId);
        List<Item> sameBrandItems;

        if (item.getBrand() != null) {
            sameBrandItems = itemDomainService.findSameBrandItem(itemId, item.getBrand().getId(), true);
        } else {
            sameBrandItems = itemDomainService.findSameBrandItem(itemId, item.getNewBrand().getId(), false);
        }

        return itemDomainService.getItemSimpleDto(user, sameBrandItems);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleDto> getTogetherScrapItem(Long userId, Long itemId) {
        User user = userDomainService.findByIdOrNull(userId);
        List<Closet> recentAddClosets = closetDomainService.getRecentAddCloset(itemId);
        List<Item> sameClosetItems = itemDomainService.getSameClosetItems(itemId, recentAddClosets);
        return itemDomainService.getItemSimpleDto(user, sameClosetItems);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleDto> getTrendItems(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);
        List<Long> blockUserIds = userBlockDomainService.getAllBlockedUser(userId).stream()
                .map(userBlock -> userBlock.getBlockedUser().getId())
                .toList();

        Page<Item> itemPage = itemDomainService.getTrendItems(blockUserIds, pageable);
        return itemDomainService.getItemSimpleDto(user, itemPage.getContent());
    }

}
