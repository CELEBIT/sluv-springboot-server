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
import com.sluv.server.domain.item.dto.HashtagResponseDto;
import com.sluv.server.domain.item.dto.HotPlaceResDto;
import com.sluv.server.domain.item.dto.ItemCategoryDto;
import com.sluv.server.domain.item.dto.ItemDetailResDto;
import com.sluv.server.domain.item.dto.ItemImgResDto;
import com.sluv.server.domain.item.dto.ItemLinkResDto;
import com.sluv.server.domain.item.dto.ItemPostReqDto;
import com.sluv.server.domain.item.dto.ItemPostResDto;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemCategory;
import com.sluv.server.domain.item.entity.ItemImg;
import com.sluv.server.domain.item.entity.ItemLike;
import com.sluv.server.domain.item.entity.ItemLink;
import com.sluv.server.domain.item.entity.RecentItem;
import com.sluv.server.domain.item.entity.hashtag.ItemHashtag;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.domain.item.exception.ItemCategoryNotFoundException;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.exception.hashtag.HashtagNotFoundException;
import com.sluv.server.domain.item.repository.ItemCategoryRepository;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemLikeRepository;
import com.sluv.server.domain.item.repository.ItemLinkRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.item.repository.RecentItemRepository;
import com.sluv.server.domain.item.repository.hashtag.HashtagRepository;
import com.sluv.server.domain.item.repository.hashtag.ItemHashtagRepository;
import com.sluv.server.domain.search.dto.SearchFilterReqDto;
import com.sluv.server.domain.user.dto.UserInfoDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.FollowRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.ai.AiModelService;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import com.sluv.server.global.common.response.PaginationResDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
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
    private final RecentItemRepository recentItemRepository;

    private final NewBrandRepository newBrandRepository;
    private final NewCelebRepository newCelebRepository;

    private final ItemLikeRepository itemLikeRepository;
    private final FollowRepository followRepository;
    private final ClosetRepository closetRepository;
    private final ItemScrapRepository itemScrapRepository;

    private final AiModelService aiModelService;

    public ItemPostResDto postItem(User user, ItemPostReqDto reqDto) {

        // 추가될 Celeb 확인
        Celeb celeb = null;
        if (reqDto.getCelebId() != null) {
            celeb = celebRepository.findById(reqDto.getCelebId())
                    .orElseThrow(CelebNotFoundException::new);
        }

        // 추가될 Brand 확인
        Brand brand = null;
        if (reqDto.getBrandId() != null) {
            brand = brandRepository.findById(reqDto.getBrandId())
                    .orElseThrow(BrandNotFoundException::new);
        }

        // 추가될 NewCeleb 확인
        NewCeleb newCeleb = null;
        if (reqDto.getNewCelebId() != null) {
            newCeleb = newCelebRepository.findById(reqDto.getNewCelebId())
                    .orElseThrow(NewCelebNotFoundException::new);
        }

        // 추가될 NewBrand 확인
        NewBrand newBrand = null;
        if (reqDto.getNewCelebId() != null) {
            newBrand = newBrandRepository.findById(reqDto.getNewBrandId())
                    .orElseThrow(NewBrandNotFoundException::new);
        }

        // 추가될 Category 확인
        ItemCategory itemCategory = itemCategoryRepository.findById(reqDto.getCategoryId())
                .orElseThrow(ItemCategoryNotFoundException::new);

        // Item을 등록인지 수정인지 판단.
        Item item =
                reqDto.getId() != null ? itemRepository.findById(reqDto.getId()).orElseThrow(ItemNotFoundException::new)
                        : null;

        Item postItem;
        // Item 수정이라면, 기존의 Item의 Id를 추가.
        if (item != null) {
            postItem = Item.toEntity(item.getId(), user, celeb, newCeleb, itemCategory, brand, newBrand, reqDto);
        } else {
            // Item 생성
            postItem = Item.toEntity(user, celeb, newCeleb, itemCategory, brand, newBrand, reqDto);
        }

        // 완성된 Item save
        Item newItem = itemRepository.save(
                postItem
        );

        // 기존의 Img, Link, Hashtag가 있다면 모두 삭제
        itemImgRepository.deleteAllByItemId(newItem.getId());
        itemLinkRepository.deleteAllByItemId(newItem.getId());
        itemHashtagRepository.deleteAllByItemId(newItem.getId());

        // ItemImg 테이블에 추가
        reqDto.getImgList().stream()
                .map(itemImg ->
                        ItemImg.toEntity(newItem, itemImg)
                ).forEach(itemImgRepository::save);

        // ItemLink 테이블에 추가
        if (reqDto.getLinkList() != null) {
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
        if (reqDto.getHashTagIdList() != null) {
            reqDto.getHashTagIdList().stream().map(hashTag ->

                    ItemHashtag.toEntity(
                            newItem,
                            hashtagRepository.findById(hashTag)
                                    .orElseThrow(HashtagNotFoundException::new)
                    )
            ).forEach(itemHashtagRepository::save);
        }

        aiModelService.getItemColor(newItem);
        return ItemPostResDto.of(newItem.getId());
    }

    @Transactional(readOnly = true)
    public List<HotPlaceResDto> getTopPlace() {

        return itemRepository.findTopPlace().stream()
                .map(HotPlaceResDto::of).toList();
    }

    public ItemDetailResDto getItemDetail(User user, Long itemId) {

        // 1. Item 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);

        item.increaseViewNum();
        recentItemRepository.save(RecentItem.of(item, user));

        // 2. Item 이미지들 조회
        List<ItemImgResDto> imgList = itemImgRepository.findAllByItemId(itemId)
                .stream()
                .map(ItemImgResDto::of).toList();

        // 3. Item Celeb
        CelebSearchResDto celeb = item.getCeleb() != null
                ? CelebSearchResDto.of(item.getCeleb())
                : null;

        String newCeleb = item.getNewCeleb() != null ?
                item.getNewCeleb().getCelebName()
                : null;

        // 4. Item Category
        ItemCategoryDto category = ItemCategoryDto.of(item.getCategory());

        // 4. Brand
        BrandSearchResDto brand = item.getBrand() != null ?
                BrandSearchResDto.of(item.getBrand())
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
                .map(ItemLinkResDto::of).toList();

        // 9. 작성자 info
        User writer = userRepository.findById(item.getUser().getId())
                .orElseThrow(UserNotFoundException::new);
        UserInfoDto writerInfo = UserInfoDto.of(writer);

        // 10. Hashtag
        List<ItemHashtag> itemHashtagId = itemHashtagRepository.findAllByItemId(itemId);
        List<HashtagResponseDto> hashtagList = itemHashtagId.stream()
                .parallel()
                .map(itemHashtag ->
                        HashtagResponseDto.of(itemHashtag.getHashtag(), null)
                ).toList();

        // 11. 같은 셀럽 아이템 리스트
        boolean celebJudge = item.getCeleb() != null;
        List<ItemSimpleResDto> sameCelebItemList = itemRepository.getItemSimpleResDto(
                user, itemRepository.findSameCelebItem(item, celebJudge)
        );

        // 12. 같은 브랜드 아이템 리스트
        boolean brandJudge = item.getBrand() != null;
        List<ItemSimpleResDto> sameBrandItemList = itemRepository.getItemSimpleResDto(
                user, itemRepository.findSameBrandItem(item, brandJudge)
        );

        // 13. 다른 스러버들이 함께 보관한 아이템 리스트
        List<ItemSimpleResDto> sameClosetItemList = itemRepository.getItemSimpleResDto(
                user, getClosetItemList(item)
        );

        // 14. 좋아요 여부
        boolean likeStatus = itemLikeRepository.existsByUserIdAndItemId(user.getId(), itemId);

        // 15. 팔로우 여부
        boolean followStatus = followRepository.getFollowStatus(user, writer);

        // 16. 스크랩 여부
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());
        boolean scrapStatus = itemScrapRepository.getItemScrapStatus(item, closetList);

        // Dto 조립
        return ItemDetailResDto.of(
                item,
                celeb,
                newCeleb,
                brand,
                newBrand,
                category,
                likeNum,
                likeStatus,
                scrapNum,
                scrapStatus,
                viewNum,
                writerInfo,
                followStatus,
                item.getUser().getId().equals(user.getId()),
                imgList,
                linkList,
                hashtagList,
                sameCelebItemList,
                sameBrandItemList,
                sameClosetItemList
        );
    }

    public void postItemLike(User user, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        boolean likeExist = itemLikeRepository.existsByUserIdAndItemId(user.getId(), itemId);
        if (!likeExist) {
            itemLikeRepository.save(
                    ItemLike.toEntity(item, user)
            );
        } else {
            itemLikeRepository.deleteByUserIdAndItemId(user.getId(), itemId);
        }

        item.decreaseViewNum();
    }

    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        item.changeStatus(ItemStatus.DELETED);
        itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<ItemSimpleResDto> getRecentItem(User user, Pageable pageable) {
        Page<Item> recentItemPage = itemRepository.getRecentItem(user, pageable);

        return convertItemSimplePageDto(user, pageable, recentItemPage);


    }

    @Transactional(readOnly = true)
    public PaginationResDto<ItemSimpleResDto> getScrapItem(User user, Pageable pageable) {
        // User, Closet, Item 조인하여 ItemPage 조회
        Page<Item> itemPage = itemRepository.getAllScrapItem(user, pageable);

        return convertItemSimplePageDto(user, pageable, itemPage);
    }

    private PaginationResDto<ItemSimpleResDto> convertItemSimplePageDto(User user, Pageable pageable, Page<Item> page) {
        // ItemPage에서 ItemSameResDto 생성
        List<ItemSimpleResDto> content = itemRepository.getItemSimpleResDto(user, page.getContent());

        return PaginationResDto.of(page, content);
    }

    private List<Item> getClosetItemList(Item item) {
        // 가장 최근에 해당 item을 추가한 상위 20개의 Closet을 검색
        List<Closet> recentAddClosetList = closetRepository.getRecentAddCloset(item);

        // 해당 Closet에 해당하는 아이템들을 최신순으로 정렬후 10개 추출.
        return itemRepository.getSameClosetItems(item, recentAddClosetList);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<ItemSimpleResDto> getRecommendItem(User user, Pageable pageable) {
        Page<Item> recommendItemPage = itemRepository.getRecommendItemPage(pageable);

        List<ItemSimpleResDto> content =
                itemRepository.getItemSimpleResDto(user, recommendItemPage.getContent());

        return PaginationResDto.of(recommendItemPage, content);
    }

    /**
     * 핫한 셀럽들이 선택한 여름나기 아이템 조회
     */
    @Transactional(readOnly = true)
    public PaginationResDto<ItemSimpleResDto> getSummerItem(User user, Pageable pageable, SearchFilterReqDto dto) {
        // itemPage 조회
        Page<Item> itemPage = itemRepository.getCelebSummerItem(pageable, dto);
        // Content 조립
        List<ItemSimpleResDto> content = itemRepository.getItemSimpleResDto(user, itemPage.getContent());

        return PaginationResDto.of(itemPage, content);
    }

    /**
     * 지금 당장 구매가능한 아이템 조회
     */
    @Transactional(readOnly = true)
    public PaginationResDto<ItemSimpleResDto> getNowBuyItem(User user, Pageable pageable, SearchFilterReqDto dto) {
        Page<Item> itemPage = itemRepository.getNowBuyItem(pageable, dto);
        List<ItemSimpleResDto> content = itemRepository.getItemSimpleResDto(user, itemPage.getContent());
        return PaginationResDto.of(itemPage, content);

    }

    @Transactional(readOnly = true)
    public PaginationResDto<ItemSimpleResDto> getNewItem(User user, Pageable pageable) {
        // itemPage 조회
        Page<Item> itemPage = itemRepository.getNewItem(pageable);

        // Content 조립
        List<ItemSimpleResDto> content = itemRepository.getItemSimpleResDto(user, itemPage.getContent());

        return PaginationResDto.of(itemPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<ItemSimpleResDto> getLuxuryItem(User user, Pageable pageable, SearchFilterReqDto dto) {
        Page<Item> itemPage = itemRepository.getLuxuryItem(pageable, dto);

        List<ItemSimpleResDto> content = itemRepository.getItemSimpleResDto(user, itemPage.getContent());

        return PaginationResDto.of(itemPage, content);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<ItemSimpleResDto> getEfficientItem(User user, Pageable pageable,
                                                               SearchFilterReqDto filterReqDto) {
        Page<Item> itemPage = itemRepository.getEfficientItem(pageable, filterReqDto);
        List<ItemSimpleResDto> content = itemRepository.getItemSimpleResDto(user, itemPage.getContent());
        return PaginationResDto.of(itemPage, content);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleResDto> getWeekHotItem(User user) {
        List<Item> itemPage = itemRepository.getWeekHotItem();
        return itemRepository.getItemSimpleResDto(user, itemPage);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleResDto> getDayHotItem(User user) {
        List<Item> itemPage = itemRepository.getDayHotItem();
        return itemRepository.getItemSimpleResDto(user, itemPage);
    }

    /**
     * 요즘 핫한 셀럽의 Item 조회
     */
    @Transactional(readOnly = true)
    public PaginationResDto<ItemSimpleResDto> getHotCelebItem(User user, Pageable pageable, SearchFilterReqDto dto) {
        Long celebId = 510L;

        Page<Item> itemPage = itemRepository.getHotCelebItem(celebId, pageable, dto);

        List<ItemSimpleResDto> content = itemRepository.getItemSimpleResDto(user, itemPage.getContent());

        return PaginationResDto.of(itemPage, content);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleResDto> getCurationItem(User user) {
        List<Celeb> interestedCeleb = celebRepository.findInterestedCeleb(user);
        List<Item> itemList = itemRepository.getCurationItem(user, interestedCeleb);

        return itemRepository.getItemSimpleResDto(user, itemList);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleResDto> getHowAboutItem(User user) {
        List<Celeb> interestedCeleb = celebRepository.findInterestedCeleb(user);
        List<Item> itemList = itemRepository.getHowAboutItem(user, interestedCeleb);

        return itemRepository.getItemSimpleResDto(user, itemList);
    }
}
