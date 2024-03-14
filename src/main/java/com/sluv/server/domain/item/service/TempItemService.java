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
import com.sluv.server.domain.item.dto.TempItemPostReqDto;
import com.sluv.server.domain.item.dto.TempItemResDto;
import com.sluv.server.domain.item.entity.ItemCategory;
import com.sluv.server.domain.item.entity.TempItem;
import com.sluv.server.domain.item.entity.TempItemImg;
import com.sluv.server.domain.item.entity.TempItemLink;
import com.sluv.server.domain.item.entity.hashtag.Hashtag;
import com.sluv.server.domain.item.entity.hashtag.TempItemHashtag;
import com.sluv.server.domain.item.exception.ItemCategoryNotFoundException;
import com.sluv.server.domain.item.exception.hashtag.HashtagNotFoundException;
import com.sluv.server.domain.item.repository.ItemCategoryRepository;
import com.sluv.server.domain.item.repository.TempItemImgRepository;
import com.sluv.server.domain.item.repository.TempItemLinkRepository;
import com.sluv.server.domain.item.repository.TempItemRepository;
import com.sluv.server.domain.item.repository.hashtag.HashtagRepository;
import com.sluv.server.domain.item.repository.hashtag.TempItemHashtagRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationCountResDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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
    private final NewBrandRepository newBrandRepository;
    private final NewCelebRepository newCelebRepository;


    public Long postTempItem(User user, TempItemPostReqDto reqDto) {
        Celeb celeb = reqDto.getCelebId() != null ? celebRepository.findById(reqDto.getCelebId())
                .orElseThrow(CelebNotFoundException::new)
                : null;
        Brand brand = reqDto.getBrandId() != null ? brandRepository.findById(reqDto.getBrandId())
                .orElseThrow(BrandNotFoundException::new)
                : null;
        NewCeleb newCeleb = reqDto.getNewCelebId() != null ? newCelebRepository.findById(reqDto.getNewCelebId())
                .orElseThrow(NewCelebNotFoundException::new)
                : null;

        NewBrand newBrand = reqDto.getNewBrandId() != null ? newBrandRepository.findById(reqDto.getNewBrandId())
                .orElseThrow(NewBrandNotFoundException::new)
                : null;
        ItemCategory itemCategory =
                reqDto.getCategoryId() != null ? itemCategoryRepository.findById(reqDto.getCategoryId())
                        .orElseThrow(ItemCategoryNotFoundException::new)
                        : null;
        TempItem tempItem = null;

        if (reqDto.getId() != null) {
            tempItem = tempItemRepository.findById(reqDto.getId())
                    .orElse(null);
        }

        TempItem postTempItem;
        if (tempItem != null) {
            postTempItem = TempItem.toEntity(tempItem.getId(), user, celeb, newCeleb, itemCategory, brand, newBrand,
                    reqDto);
        } else {
            postTempItem = TempItem.toEntity(user, celeb, newCeleb, itemCategory, brand, newBrand, reqDto);
        }

        TempItem saveTempItem = tempItemRepository.save(postTempItem);

        // ItemImg 테이블에 추가
        tempItemImgRepository.deleteAllByTempItemId(postTempItem.getId());
        if (reqDto.getImgList() != null) {
            reqDto.getImgList().stream()
                    .map(tempItemImg ->
                            TempItemImg.toEntity(saveTempItem, tempItemImg)
                    ).forEach(tempItemImgRepository::save);

        }

        // ItemLink 테이블에 추가
        tempItemLinkRepository.deleteAllByTempItemId(postTempItem.getId());
        if (reqDto.getLinkList() != null) {
            reqDto.getLinkList().stream()
                    .map(tempItemLink ->
                            TempItemLink.toEntity(saveTempItem, tempItemLink)
                    ).forEach(tempItemLinkRepository::save);
        }

        // ItemHashtag 테이블에 추가
        tempItemHashtagRepository.deleteAllByTempItemId(postTempItem.getId());
        if (reqDto.getHashTagIdList() != null) {
            reqDto.getHashTagIdList().stream().map(hashTag -> {
                Hashtag tag = hashtagRepository.findById(hashTag).orElseThrow(HashtagNotFoundException::new);
                return TempItemHashtag.toEntity(saveTempItem, tag);
            }).forEach(tempItemHashtagRepository::save);
        }

        return saveTempItem.getId();
    }

    @Transactional(readOnly = true)
    public PaginationCountResDto<TempItemResDto> getTempItemList(User user, Pageable pageable) {
        Page<TempItem> tempItemPage = tempItemRepository.getTempItemList(user, pageable);

        List<TempItemResDto> dtoList = tempItemRepository.getTempItemResDto(tempItemPage.getContent());

        return new PaginationCountResDto<>(
                tempItemPage.hasNext(),
                tempItemPage.getNumber(),
                dtoList,
                tempItemPage.getTotalElements());


    }

    public void deleteTempItem(Long tempItemId) {
        // 관련된 삭제
        // 1. tempItemImg 삭제
        tempItemImgRepository.deleteAllByTempItemId(tempItemId);
        // 2. tempItemLink 삭제
        tempItemLinkRepository.deleteAllByTempItemId(tempItemId);
        // 3. tempItemHashtag 삭제
        tempItemHashtagRepository.deleteAllByTempItemId(tempItemId);

        // tempItem 삭제
        tempItemRepository.deleteById(tempItemId);
    }

    public void deleteAllTempItem(User user) {
        // 1. 해당 유저의 모든 TempItem 조회
        List<TempItem> tempItemList = tempItemRepository.findAllExceptLast(user);

        // 2. 모든 TempItem에 대한 관련된 삭제
        tempItemList.forEach(tempItem -> {
            // 2-1. tempItemImg 삭제
            tempItemImgRepository.deleteAllByTempItemId(tempItem.getId());
            // 2-2. tempItemLink 삭제ㅎ
            tempItemLinkRepository.deleteAllByTempItemId(tempItem.getId());
            // 2-3. tempItemHashtag 삭제
            tempItemHashtagRepository.deleteAllByTempItemId(tempItem.getId());
            // 3. TempItem 삭제
            tempItemRepository.deleteById(tempItem.getId());
        });

    }

}
