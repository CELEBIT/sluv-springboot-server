package com.sluv.server.domain.search.service;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.repository.ClosetRepository;
import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.repository.ItemImgRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.ItemScrapRepository;
import com.sluv.server.domain.search.utils.ElasticSearchConnectUtil;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemScrapRepository itemScrapRepository;

    private final ClosetRepository closetRepository;
    private final ElasticSearchConnectUtil elasticSearchConnectUtil;

    /**
     * Item 검색 with ElasticSearch
     */
    public PaginationResDto<ItemSimpleResDto> getSearchItem(User user, String keyword, Pageable pageable) {
        // ElasticSearch API Path
        String itemPath = "/search/searchItem";

        // ElasticSearch 에서 Keyword에 해당하는 Item의 Id 조회
        List<Long> itemIdList = elasticSearchConnectUtil.connectElasticSearch(keyword, itemPath);

        // 조건에 맞는 Item Page 조회
        Page<Item> searchItemPage = itemRepository.getSearchItem(itemIdList, pageable);

        // 현재 접속한 User의 옷장 목록 조회
        List<Closet> closetList = closetRepository.findAllByUserId(user.getId());

        // Cotent 조립
        List<ItemSimpleResDto> content = searchItemPage.stream().map(item ->
                ItemSimpleResDto.builder()
                        .itemId(item.getId())
                        .imgUrl(itemImgRepository.findMainImg(item.getId()).getItemImgUrl())
                        .brandName(
                                item.getBrand() != null
                                        ? item.getBrand().getBrandKr()
                                        : item.getNewBrand().getBrandName()
                        )
                        .itemName(item.getName())
                        .celebName(
                                item.getCeleb() != null
                                        ? item.getCeleb().getCelebNameKr()
                                        : item.getNewCeleb().getCelebName()
                        )
                        .scrapStatus(itemScrapRepository.getItemScrapStatus(item, closetList))
                        .build()
        ).toList();


        return PaginationResDto.<ItemSimpleResDto>builder()
                .page(searchItemPage.getNumber())
                .hasNext(searchItemPage.hasNext())
                .content(content)
                .build();
    }

}
