package com.sluv.api.search.service;

import com.sluv.api.brand.dto.response.BrandSearchResponse;
import com.sluv.api.celeb.dto.response.CelebSearchResponse;
import com.sluv.api.common.response.PaginationResponse;
import com.sluv.api.item.dto.ItemKeywordSearchResDto;
import com.sluv.api.search.dto.RecentSearchChipResDto;
import com.sluv.api.search.dto.SearchKeywordResDto;
import com.sluv.api.search.dto.SearchKeywordTotalResDto;
import com.sluv.domain.brand.service.BrandDomainService;
import com.sluv.domain.celeb.service.CelebDomainService;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.search.entity.SearchData;
import com.sluv.domain.search.entity.SearchRank;
import com.sluv.domain.search.service.RecentSearchDomainService;
import com.sluv.domain.search.service.SearchDataDomainService;
import com.sluv.domain.search.service.SearchRankDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final RecentSearchDomainService recentSearchDomainService;
    private final SearchRankDomainService searchRankDomainService;
    private final SearchDataDomainService searchDataDomainService;
    private final CelebDomainService celebDomainService;
    private final BrandDomainService brandDomainService;
    private final ItemDomainService itemDomainService;
    private final UserDomainService userDomainService;

    @Transactional(readOnly = true)
    public List<RecentSearchChipResDto> getRecentSearch(Long userId) {
        User user = userDomainService.findById(userId);
        List<RecentSearchChipResDto> result = recentSearchDomainService.getRecentSearch(user)
                .stream()
                .map(recentSearch -> RecentSearchChipResDto.of(recentSearch.getSearchWord()))
                .toList();

        return result;
    }

    @Transactional(readOnly = true)
    public List<SearchKeywordResDto> getSearchRank() {
        List<SearchRank> searchRankList = searchRankDomainService.findAllByOrderBySearchCountDesc();
        return searchRankList
                .stream()
                .map(searchRank -> SearchKeywordResDto.of(searchRank.getSearchWord()))
                .toList();
    }

    @Transactional(readOnly = true)
    public PaginationResponse<SearchKeywordResDto> getSearchKeyword(String keyword, Pageable pageable) {
        Page<SearchData> searchDataPage = searchDataDomainService.getSearchKeyword(keyword, pageable);

        List<SearchKeywordResDto> content = searchDataPage.stream()
                .map(searchData -> SearchKeywordResDto.of(searchData.getSearchWord()))
                .toList();

        return PaginationResponse.of(searchDataPage, content);
    }

    @Async
    @Transactional
    public void postRecentSearch(Long userId, String keyword) {
        User user = userDomainService.findById(userId);
        if (!keyword.isEmpty() && !keyword.isBlank()) {
            log.info("Post RecentSearch -> User: {}, Keyword: {}", user.getId(), keyword);
            recentSearchDomainService.saveRecentSearch(user, keyword);
        }
    }

    @Async
    @Transactional
    public void postSearchData(String keyword) {
        if (!keyword.isEmpty() && !keyword.isBlank()) {
            log.info("Post SearchData -> Keyword: {}", keyword);
            searchDataDomainService.saveSearchData(keyword);
        }
    }

    /**
     * 현재 유저의 RecentSearch 키워드 삭제
     */
    @Transactional
    public void deleteSearchKeyword(Long userId, String keyword) {
        log.info("Delete {}'s Recent Search Keyword: {} ", userId, keyword);
        recentSearchDomainService.deleteByUserIdAndSearchWord(userId, keyword);
    }

    @Transactional
    public void deleteAllSearchKeyword(Long userId) {
        log.info("Delete {}'s All Recent Search Keyword", userId);
        recentSearchDomainService.deleteAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public SearchKeywordTotalResDto getAllDateByKeyword(Long userId, String keyword) {
        PageRequest pageable = PageRequest.of(0, 5);
        // 1. 셀럽 2. 브랜드 3. 아이템
        List<CelebSearchResponse> celebByContainKeyword = celebDomainService.getCelebByContainKeyword(keyword, pageable).stream()
                .map(CelebSearchResponse::of)
                .toList();

        List<BrandSearchResponse> brandByContainKeyword = brandDomainService.getBrandContainKeyword(keyword).stream()
                .map(BrandSearchResponse::from)
                .toList();

        List<ItemKeywordSearchResDto> itemByContainKeyword = itemDomainService.getItemContainKeyword(keyword).stream()
                .map(ItemKeywordSearchResDto::of)
                .toList();

        return SearchKeywordTotalResDto.of(celebByContainKeyword, brandByContainKeyword, itemByContainKeyword);
    }
}
