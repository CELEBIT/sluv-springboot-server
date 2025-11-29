package com.sluv.api.domain.item.service;

import com.sluv.api.brand.dto.response.BrandSearchResponse;
import com.sluv.api.celeb.dto.response.CelebSearchResponse;
import com.sluv.api.item.dto.ItemCategoryDto;
import com.sluv.api.item.dto.ItemDetailFixData;
import com.sluv.api.item.dto.ItemDetailResDto;
import com.sluv.api.item.service.ItemService;
import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.CelebCategory;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.service.ClosetDomainService;
import com.sluv.domain.item.dto.ItemCountDto;
import com.sluv.domain.item.dto.ItemStatusDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemCategory;
import com.sluv.domain.item.enums.ItemStatus;
import com.sluv.domain.item.exception.ItemNotActiveException;
import com.sluv.domain.item.service.*;
import com.sluv.domain.user.dto.UserInfoDto;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.cache.CacheService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemDomainService itemDomainService;
    @Mock
    private ItemImgDomainService itemImgDomainService;
    @Mock
    private ItemLinkDomainService itemLinkDomainService;
    @Mock
    private ItemHashtagDomainService itemHashtagDomainService;
    @Mock
    private UserDomainService userDomainService;
    @Mock
    private RecentItemDomainService recentItemDomainService;
    @Mock
    private ClosetDomainService closetDomainService;
    @Mock
    private CacheService<ItemDetailFixData> cacheService;


    @Test
    @DisplayName("아이템 상세 조회 - 캐시 미스")
    void getItemDetail_cacheMiss() {
        // given
        Long userId = 1L;
        Long itemId = 100L;

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Item item = mock(Item.class);
        when(item.getId()).thenReturn(itemId);
        when(item.getItemStatus()).thenReturn(ItemStatus.ACTIVE);
        when(item.getViewNum()).thenReturn(10L);
        when(item.getUser()).thenReturn(user);
        when(item.getCategory()).thenReturn(mock(ItemCategory.class));

        when(userDomainService.findByIdOrNull(userId)).thenReturn(user);
        when(itemDomainService.findByIdForDetail(itemId)).thenReturn(item);
        when(cacheService.findByKey(anyString())).thenReturn(null);
        when(itemImgDomainService.findAllByItemId(itemId)).thenReturn(Collections.emptyList());
        when(itemLinkDomainService.findAllByItemId(itemId)).thenReturn(Collections.emptyList());
        when(itemHashtagDomainService.findAllByItemId(itemId)).thenReturn(Collections.emptyList());
        when(itemDomainService.getCountDataByItemId(itemId)).thenReturn(new ItemCountDto(50, 20));

        Closet closet = mock(Closet.class);
        when(closet.getId()).thenReturn(1L);
        when(closetDomainService.findAllByUserId(userId)).thenReturn(List.of(closet));

        ItemStatusDto statusDto = new ItemStatusDto(true, true, false);
        when(itemDomainService.getStatusDataByItemId(eq(itemId), any(User.class), anyList())).thenReturn(statusDto);

        // when
        ItemDetailResDto result = itemService.getItemDetail(userId, itemId);

        // then
        assertThat(result).isNotNull();
        verify(item).increaseViewNum();
        verify(recentItemDomainService).saveRecentItem(item, user);
        verify(cacheService).saveWithKey(anyString(), any(ItemDetailFixData.class));
    }

    @Test
    @DisplayName("아이템 상세 조회 - 캐시 히트")
    void getItemDetail_cacheHit() {
        // given
        Long userId = 1L;
        Long itemId = 100L;

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Item item = mock(Item.class);
        when(item.getItemStatus()).thenReturn(ItemStatus.ACTIVE);
        when(item.getViewNum()).thenReturn(10L);
        when(item.getUser()).thenReturn(user);

        ItemDetailFixData fixData = mock(ItemDetailFixData.class);

        Celeb celeb = mock(Celeb.class);
        CelebCategory celebCategory = mock(CelebCategory.class);
        when(celeb.getCelebCategory()).thenReturn(celebCategory);
        CelebSearchResponse celebRes = CelebSearchResponse.of(celeb);
        when(fixData.getCeleb()).thenReturn(celebRes);
        when(fixData.getNewCeleb()).thenReturn(null);

        Brand brand = mock(Brand.class);
        BrandSearchResponse brandRes = BrandSearchResponse.of(brand);
        when(fixData.getBrand()).thenReturn(brandRes);
        when(fixData.getNewBrand()).thenReturn(null);

        ItemCategory itemCategory = mock(ItemCategory.class);
        ItemCategoryDto categoryDto = ItemCategoryDto.of(itemCategory);
        when(fixData.getCategory()).thenReturn(categoryDto);

        UserInfoDto writerDto = UserInfoDto.of(user);
        when(fixData.getWriter()).thenReturn(writerDto);

        when(fixData.getImgList()).thenReturn(Collections.emptyList());
        when(fixData.getLinkList()).thenReturn(Collections.emptyList());
        when(fixData.getHashTagList()).thenReturn(Collections.emptyList());

        when(cacheService.findByKey(anyString())).thenReturn(fixData);

        when(userDomainService.findByIdOrNull(userId)).thenReturn(user);
        when(itemDomainService.findByIdForDetail(itemId)).thenReturn(item);
        when(itemDomainService.getCountDataByItemId(itemId))
                .thenReturn(new ItemCountDto(50, 20));

        Closet closet = mock(Closet.class);
        when(closet.getId()).thenReturn(1L);
        when(closetDomainService.findAllByUserId(userId)).thenReturn(List.of(closet));

        ItemStatusDto statusDto = new ItemStatusDto(true, true, false);
        when(itemDomainService.getStatusDataByItemId(eq(itemId), any(User.class), anyList()))
                .thenReturn(statusDto);

        // when
        ItemDetailResDto result = itemService.getItemDetail(userId, itemId);

        // then
        assertThat(result).isNotNull();

        verify(item).increaseViewNum();
        verify(recentItemDomainService).saveRecentItem(item, user);

        verify(itemImgDomainService, never()).findAllByItemId(anyLong());
        verify(itemLinkDomainService, never()).findAllByItemId(anyLong());
        verify(itemHashtagDomainService, never()).findAllByItemId(anyLong());
        verify(cacheService, never()).saveWithKey(anyString(), any(ItemDetailFixData.class));
    }


    @Test
    @DisplayName("비활성 아이템 조회 시 예외 발생")
    void getItemDetail_ItemNotActive() {
        // given
        Long itemId = 100L;
        Item item = mock(Item.class);
        when(item.getItemStatus()).thenReturn(ItemStatus.DELETED);
        when(itemDomainService.findByIdForDetail(itemId)).thenReturn(item);
        when(userDomainService.findByIdOrNull(null)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> itemService.getItemDetail(null, itemId))
                .isInstanceOf(ItemNotActiveException.class);
    }

}