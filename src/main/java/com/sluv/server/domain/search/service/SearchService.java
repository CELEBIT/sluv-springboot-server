package com.sluv.server.domain.search.service;

import com.sluv.server.domain.search.dto.RecentSearchChipResDto;
import com.sluv.server.domain.search.dto.SearchKeywordResDto;
import com.sluv.server.domain.search.entity.RecentSearch;
import com.sluv.server.domain.search.entity.SearchData;
import com.sluv.server.domain.search.entity.SearchRank;
import com.sluv.server.domain.search.repository.RecentSearchRepository;
import com.sluv.server.domain.search.repository.SearchDataRepository;
import com.sluv.server.domain.search.repository.SearchRankRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.response.PaginationResDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final RecentSearchRepository recentSearchRepository;
    private final SearchRankRepository searchRankRepository;
    private final SearchDataRepository searchDataRepository;

    @Transactional(readOnly = true)
    public List<RecentSearchChipResDto> getRecentSearch(User user) {
        List<RecentSearchChipResDto> result
                = recentSearchRepository.getRecentSearch(user)
                .stream()
                .map(recentSearch ->
                        RecentSearchChipResDto.of(recentSearch.getSearchWord())
                ).toList();

        return result;
    }

    @Transactional(readOnly = true)
    public List<SearchKeywordResDto> getSearchRank() {
        List<SearchRank> searchRankList = searchRankRepository.findAllByOrderBySearchCountDesc();
        return searchRankList
                .stream()
                .map(searchRank ->
                        SearchKeywordResDto.of(searchRank.getSearchWord())
                ).toList();
    }

    @Transactional(readOnly = true)
    public PaginationResDto<SearchKeywordResDto> getSearchKeyword(String keyword, Pageable pageable) {
        Page<SearchData> searchDataPage = searchDataRepository.getSearchKeyword(keyword, pageable);

        List<SearchKeywordResDto> content = searchDataPage.stream()
                .map(searchData ->
                        SearchKeywordResDto.of(searchData.getSearchWord()
                        )
                ).toList();

        return PaginationResDto.of(searchDataPage, content);
    }

    @Async
    @Transactional
    public void postRecentSearch(User user, String keyword) {
        RecentSearch recentSearch = RecentSearch.of(user, keyword);

        log.info("Post RecentSearch -> User: {}, Keyword: {}", user.getId(), keyword);
        recentSearchRepository.save(recentSearch);

    }

    @Async
    @Transactional
    public void postSearchData(String keyword) {
        SearchData searchData = SearchData.of(keyword);

        log.info("Post SearchData -> Keyword: {}", keyword);
        searchDataRepository.save(searchData);

    }

    /**
     * 현재 유저의 RecentSearch 키워드 삭제
     */
    @Transactional
    public void deleteSearchKeyword(User user, String keyword) {
        log.info("Delete {}'s Recent Search Keyword: {} ", user.getId(), keyword);
        recentSearchRepository.deleteByUserIdAndSearchWord(user.getId(), keyword);
    }

    @Transactional
    public void deleteAllSearchKeyword(User user) {
        log.info("Delete {}'s All Recent Search Keyword", user.getId());
        recentSearchRepository.deleteAllByUserId(user.getId());
    }
}
