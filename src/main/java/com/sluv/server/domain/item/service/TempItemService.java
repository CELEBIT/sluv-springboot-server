package com.sluv.server.domain.item.service;

import com.sluv.server.domain.brand.dto.NewBrandPostResDto;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.brand.exception.BrandNotFoundException;
import com.sluv.server.domain.brand.exception.NewBrandNotFoundException;
import com.sluv.server.domain.brand.repository.BrandRepository;
import com.sluv.server.domain.brand.repository.NewBrandRepository;
import com.sluv.server.domain.celeb.dto.CelebDto;
import com.sluv.server.domain.celeb.dto.NewCelebPostResDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.celeb.exception.NewCelebNotFoundException;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import com.sluv.server.domain.item.dto.*;
import com.sluv.server.domain.item.entity.*;
import com.sluv.server.domain.item.entity.hashtag.Hashtag;
import com.sluv.server.domain.item.entity.hashtag.TempItemHashtag;
import com.sluv.server.domain.item.enums.ItemStatus;
import com.sluv.server.domain.item.exception.ItemCategoryNotFoundException;
import com.sluv.server.domain.item.exception.TempItemNotFoundException;
import com.sluv.server.domain.item.exception.hashtag.HashtagNotFoundException;
import com.sluv.server.domain.item.repository.*;
import com.sluv.server.domain.item.repository.hashtag.HashtagRepository;
import com.sluv.server.domain.item.repository.hashtag.TempItemHashtagRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.enums.ItemImgOrLinkStatus;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        ItemCategory itemCategory = reqDto.getCategoryId() != null ? itemCategoryRepository.findById(reqDto.getCategoryId())
                .orElseThrow(ItemCategoryNotFoundException::new)
                : null;

        TempItem tempItem = reqDto.getId() != null ? tempItemRepository.findById(reqDto.getId())
                .orElseThrow(TempItemNotFoundException::new)
                :TempItem.builder().itemStatus(ItemStatus.ACTIVE).build();

        tempItem.setUser(user);
        tempItem.setCeleb(celeb);
        tempItem.setNewCeleb(newCeleb);
        tempItem.setCategory(itemCategory);
        tempItem.setBrand(brand);
        tempItem.setNewBrand(newBrand);
        tempItem.setName(reqDto.getItemName());
        tempItem.setWhenDiscovery(reqDto.getWhenDiscovery());
        tempItem.setWhereDiscovery(reqDto.getWhereDiscovery());
        tempItem.setPrice(reqDto.getPrice());
        tempItem.setAdditionalInfo(reqDto.getAdditionalInfo());
        tempItem.setInfoSource(reqDto.getInfoSource());

        TempItem saveTempItem = tempItemRepository.save(tempItem);

        // ItemImg 테이블에 추가
        tempItemImgRepository.deleteAllByTempItemId(tempItem.getId());
        if(reqDto.getImgList() != null) {
            reqDto.getImgList().stream()
                            .map(tempItemImg ->
                                TempItemImg.builder()
                                        .tempItem(saveTempItem)
                                        .tempItemImgUrl(tempItemImg.getImgUrl())
                                        .representFlag(tempItemImg.getRepresentFlag())
                                        .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
                                        .build()
                            ).forEach(tempItemImgRepository::save);

        }

        // ItemLink 테이블에 추가
        tempItemLinkRepository.deleteAllByTempItemId(tempItem.getId());
        if(reqDto.getLinkList() != null) {
            reqDto.getLinkList().stream()
                            .map(tempItemLink ->
                                    TempItemLink.builder()
                                            .tempItem(saveTempItem)
                                            .linkName(tempItemLink.getLinkName())
                                            .tempItemLinkUrl(tempItemLink.getItemLinkUrl())
                                            .itemImgOrLinkStatus(ItemImgOrLinkStatus.ACTIVE)
                                            .build()

                            ).forEach(tempItemLinkRepository::save);
        }

        // ItemHashtag 테이블에 추가
        tempItemHashtagRepository.deleteAllByTempItemId(tempItem.getId());
        if(reqDto.getHashTagIdList() != null) {
            reqDto.getHashTagIdList().stream().map(hashTag ->
                    TempItemHashtag.builder()
                            .tempItem(saveTempItem)
                            .hashtag(
                                    hashtagRepository.findById(hashTag)
                                            .orElseThrow(HashtagNotFoundException::new)
                            )
                            .build()

            ).forEach(tempItemHashtagRepository::save);
        }

        return saveTempItem.getId();
    }

    public TempItemPageDto<TempItemResDto> getTempItemList(User user, Pageable pageable){

        Page<TempItem> contentPage = tempItemRepository.getTempItemList(user, pageable);

        List<TempItemResDto> dtoList = contentPage.stream().map(tempItem -> {

                    List<ItemImgResDto> tempImgList = tempItemImgRepository.findAllByTempItem(tempItem)
                            .stream().map(tempItemImg -> ItemImgResDto.builder()
                                    .imgUrl(tempItemImg.getTempItemImgUrl())
                                    .representFlag(tempItemImg.getRepresentFlag())
                                    .build()
                            ).collect(Collectors.toList());

                    List<Hashtag> tempHashtagList = tempItemHashtagRepository.findAllByTempItem(tempItem)
                            .stream().map(TempItemHashtag::getHashtag).toList();

                    List<ItemLinkResDto> tempLinkList = tempItemLinkRepository.findAllByTempItem(tempItem)
                            .stream().map(tempItemLink -> ItemLinkResDto.builder()
                                    .linkName(tempItemLink.getLinkName())
                                    .itemLinkUrl(tempItemLink.getTempItemLinkUrl())
                                    .build()
                            ).collect(Collectors.toList());

                    CelebDto celebDto = tempItem.getCeleb() != null ?
                            CelebDto.builder()
                                    .id(tempItem.getCeleb().getId())
                                    .celebNameKr(tempItem.getCeleb().getCelebNameKr())
                                    .celebNameEn(tempItem.getCeleb().getCelebNameEn())
                                    .categoryChild(tempItem.getCeleb().getCelebCategory().getName())
                                    .categoryParent(tempItem.getCeleb().getCelebCategory().getParent().getName())
                                    .parentCelebNameKr(tempItem.getCeleb().getParent() != null ? tempItem.getCeleb().getParent().getCelebNameKr() : null)
                                    .parentCelebNameEn(tempItem.getCeleb().getParent() != null ? tempItem.getCeleb().getParent().getCelebNameEn() : null)
                                    .build()
                            : null;

                    ItemCategoryDto itemCategoryDto = tempItem.getCategory() != null ?
                            ItemCategoryDto.builder()
                                    .id(tempItem.getCategory().getId())
                                    .name(tempItem.getCategory().getName())
                                    .parentName(
                                            tempItem.getCategory().getParent() != null
                                                    ? tempItem.getCategory().getParent().getName()
                                                    : null
                                    )
                                    .parentId(
                                            tempItem.getCategory().getParent() != null
                                                    ? tempItem.getCategory().getParent().getId()
                                                    : null
                                    )
                                    .build()
                            : null;

                    Brand brand = tempItem.getBrand() != null
                            ? tempItem.getBrand()
                            : null;

                    return TempItemResDto.builder()
                            .id(tempItem.getId())
                            .imgList(tempImgList)
                            .celeb(celebDto)
                            .brand(brand)
                            .whenDiscovery(tempItem.getWhenDiscovery())
                            .whereDiscovery(tempItem.getWhereDiscovery())
                            .category(itemCategoryDto)
                            .itemName(tempItem.getName())
                            .price(tempItem.getPrice())
                            .additionalInfo(tempItem.getAdditionalInfo())
                            .hashTagList(tempHashtagList)
                            .linkList(tempLinkList)
                            .infoSource(tempItem.getInfoSource())
                            .newCeleb(
                                    tempItem.getNewCeleb() != null
                                    ? NewCelebPostResDto.builder()
                                            .newCelebId(tempItem.getNewCeleb().getId())
                                            .newCelebName(tempItem.getNewCeleb().getCelebName())
                                            .build()
                                    :null
                            )
                            .newBrand(
                                    tempItem.getNewBrand() != null
                                    ? NewBrandPostResDto.builder()
                                            .newBrandId(tempItem.getNewBrand().getId())
                                            .newBrandName(tempItem.getNewBrand().getBrandName())
                                            .build()
                                    :null
                                    )
                            .updatedAt(tempItem.getUpdatedAt())
                            .build();

                }
        ).toList();

        return new TempItemPageDto<TempItemResDto>(
                contentPage.hasNext(),
                contentPage.getNumber(),
                dtoList,
                contentPage.getTotalElements());


    }

    @Transactional
    public void deleteTempItem(Long tempItemId){
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

    @Transactional
    public void deleteAllTempItem(User user){
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
