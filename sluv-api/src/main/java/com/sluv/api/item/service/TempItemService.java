package com.sluv.api.item.service;

import com.sluv.api.common.response.PaginationCountResponse;
import com.sluv.api.item.dto.ItemHashtagResponseDto;
import com.sluv.api.item.dto.TempItemCountResDto;
import com.sluv.api.item.dto.TempItemPostReqDto;
import com.sluv.api.item.dto.TempItemResDto;
import com.sluv.api.item.helper.ItemHelper;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.NewBrand;
import com.sluv.domain.brand.service.BrandDomainService;
import com.sluv.domain.brand.service.NewBrandDomainService;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.service.CelebDomainService;
import com.sluv.domain.celeb.service.NewCelebDomainService;
import com.sluv.domain.item.dto.ItemImgDto;
import com.sluv.domain.item.dto.ItemLinkDto;
import com.sluv.domain.item.dto.TempItemSaveDto;
import com.sluv.domain.item.entity.ItemCategory;
import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.TempItemImg;
import com.sluv.domain.item.entity.TempItemLink;
import com.sluv.domain.item.entity.hashtag.Hashtag;
import com.sluv.domain.item.entity.hashtag.TempItemHashtag;
import com.sluv.domain.item.service.ItemCategoryDomainService;
import com.sluv.domain.item.service.TempItemDomainService;
import com.sluv.domain.item.service.TempItemImgDomainService;
import com.sluv.domain.item.service.TempItemLinkDomainService;
import com.sluv.domain.item.service.hashtag.HashtagDomainService;
import com.sluv.domain.item.service.hashtag.TempItemHashtagDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TempItemService {

    private final UserDomainService userDomainService;
    private final TempItemDomainService tempItemDomainService;
    private final TempItemLinkDomainService tempItemLinkDomainService;
    private final TempItemImgDomainService tempItemImgDomainService;
    private final TempItemHashtagDomainService tempItemHashtagDomainService;

    private final HashtagDomainService hashtagDomainService;
    private final CelebDomainService celebDomainService;
    private final ItemCategoryDomainService itemCategoryDomainService;
    private final BrandDomainService brandDomainService;
    private final NewBrandDomainService newBrandDomainService;
    private final NewCelebDomainService newCelebDomainService;

    private final ItemHelper itemHelper;


    public Long postTempItem(Long userId, TempItemPostReqDto reqDto) {
        User user = userDomainService.findById(userId);
        Celeb celeb = reqDto.getCelebId() != null ? celebDomainService.findById(reqDto.getCelebId())
                : null;
        Brand brand = reqDto.getBrandId() != null ? brandDomainService.findById(reqDto.getBrandId())
                : null;
        NewCeleb newCeleb = reqDto.getNewCelebId() != null ? newCelebDomainService.findById(reqDto.getNewCelebId())
                : null;

        NewBrand newBrand = reqDto.getNewBrandId() != null ? newBrandDomainService.findById(reqDto.getNewBrandId())
                : null;
        ItemCategory itemCategory =
                reqDto.getCategoryId() != null ? itemCategoryDomainService.findById(reqDto.getCategoryId())
                        : null;

        TempItem tempItem = null;

        if (reqDto.getId() != null) {
            tempItem = tempItemDomainService.findByIdOrNull(reqDto.getId());
        }

        TempItemSaveDto tempItemSaveDto = itemHelper.convertTempItemPostReqDtoToTempItemSaveDto(reqDto);

        TempItem postTempItem;
        if (tempItem != null) {
            postTempItem = TempItem.toEntity(tempItem.getId(), user, celeb, newCeleb, itemCategory, brand, newBrand,
                    tempItemSaveDto);
        } else {
            postTempItem = TempItem.toEntity(user, celeb, newCeleb, itemCategory, brand, newBrand, tempItemSaveDto);
        }

        TempItem saveTempItem = tempItemDomainService.saveTempItem(postTempItem);

        // ItemImg 테이블에 추가
        tempItemImgDomainService.deleteAllByTempItemId(postTempItem.getId());
        if (reqDto.getImgList() != null) {
            reqDto.getImgList()
                    .forEach(tempItemImg -> tempItemImgDomainService.saveTempItemImg(saveTempItem, tempItemImg));
        }

        // ItemLink 테이블에 추가
        tempItemLinkDomainService.deleteAllByTempItemId(postTempItem.getId());
        if (reqDto.getLinkList() != null) {
            reqDto.getLinkList()
                    .forEach(tempItemLink -> tempItemLinkDomainService.saveTempItemLink(saveTempItem, tempItemLink));
        }

        // ItemHashtag 테이블에 추가
        tempItemHashtagDomainService.deleteAllByTempItemId(postTempItem.getId());
        if (reqDto.getHashTagIdList() != null) {
            reqDto.getHashTagIdList().forEach(hashTag -> {
                Hashtag tag = hashtagDomainService.findById(hashTag);
                tempItemHashtagDomainService.saveTempItemHashtag(saveTempItem, tag);
            });
        }

        return saveTempItem.getId();
    }

    @Transactional(readOnly = true)
    public PaginationCountResponse<TempItemResDto> getTempItemList(Long userId, Pageable pageable) {
        User user = userDomainService.findById(userId);
        Page<TempItem> tempItemPage = tempItemDomainService.getTempItemList(user, pageable);

        List<TempItemImg> tempItemImages = tempItemImgDomainService.getTempItemImages(tempItemPage.getContent());
        List<TempItemLink> tempItemLinks = tempItemLinkDomainService.getTempItemLinks(tempItemPage.getContent());
        List<TempItemHashtag> tempItemHashtags = tempItemHashtagDomainService.getTempItemHashtags(
                tempItemPage.getContent());

        Map<Long, List<ItemImgDto>> tempItemImgMap = tempItemImages.stream().collect(
                Collectors.groupingBy(ti -> ti.getTempItem().getId(),
                        Collectors.mapping(ItemImgDto::of, Collectors.toList())));

        Map<Long, List<ItemLinkDto>> tempItemLinkMap = tempItemLinks.stream().collect(
                Collectors.groupingBy(tl -> tl.getTempItem().getId(),
                        Collectors.mapping(ItemLinkDto::of, Collectors.toList())));

        Map<Long, List<Hashtag>> tempItemHashtagMap = tempItemHashtags.stream().collect(
                Collectors.groupingBy(th -> th.getTempItem().getId(),
                        Collectors.mapping(TempItemHashtag::getHashtag, Collectors.toList())));

        List<TempItemResDto> dtos = tempItemPage.getContent().stream().map(ti -> {
            List<ItemHashtagResponseDto> hashtags = null;
            if (tempItemHashtagMap.get(ti.getId()) != null) {
                hashtags = tempItemHashtagMap.get(ti.getId()).stream()
                        .map(ItemHashtagResponseDto::of).toList();
            }
            return TempItemResDto.of(ti, tempItemImgMap.get(ti.getId()), tempItemLinkMap.get(ti.getId()), hashtags);
        }).toList();

        return new PaginationCountResponse<>(
                tempItemPage.hasNext(),
                tempItemPage.getNumber(),
                dtos,
                tempItemPage.getTotalElements());


    }

    public void deleteTempItem(Long tempItemId) {
        // 관련된 삭제
        // 1. tempItemImg 삭제
        tempItemImgDomainService.deleteAllByTempItemId(tempItemId);
        // 2. tempItemLink 삭제
        tempItemLinkDomainService.deleteAllByTempItemId(tempItemId);
        // 3. tempItemHashtag 삭제
        tempItemHashtagDomainService.deleteAllByTempItemId(tempItemId);

        // tempItem 삭제
        tempItemDomainService.deleteById(tempItemId);
    }

    public void deleteAllTempItem(Long userId) {
        // 1. 해당 유저의 모든 TempItem 조회
        List<TempItem> tempItemList = tempItemDomainService.findAllExceptLast(userId);

        // 2. 모든 TempItem에 대한 관련된 삭제
        tempItemList.forEach(tempItem -> {
            // 2-1. tempItemImg 삭제
            tempItemImgDomainService.deleteAllByTempItemId(tempItem.getId());
            // 2-2. tempItemLink 삭제ㅎ
            tempItemLinkDomainService.deleteAllByTempItemId(tempItem.getId());
            // 2-3. tempItemHashtag 삭제
            tempItemHashtagDomainService.deleteAllByTempItemId(tempItem.getId());
            // 3. TempItem 삭제
            tempItemDomainService.deleteById(tempItem.getId());
        });

    }

    public TempItemCountResDto countTempItemCount(Long userId) {
        return TempItemCountResDto.of(tempItemDomainService.countByUserId(userId));
    }

}
