package com.sluv.domain.search.service;

import com.querydsl.core.Tuple;
import com.sluv.domain.search.entity.SearchData;
import com.sluv.domain.search.entity.SearchRank;
import com.sluv.domain.search.repository.SearchDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchDataDomainService {

    private final SearchDataRepository searchDataRepository;

    public Page<SearchData> getSearchKeyword(String keyword, Pageable pageable) {
        return searchDataRepository.getSearchKeyword(keyword, pageable);
    }

    public void saveSearchData(String keyword) {
        SearchData searchData = SearchData.of(keyword);
        searchDataRepository.save(searchData);
    }

    public List<SearchRank> getTopData() {
        List<Tuple> topData = searchDataRepository.getTopData();
        return topData.stream().map(data ->
                SearchRank.of(
                        data.get(1, Long.class),
                        data.get(0, SearchData.class).getSearchWord()
                )).toList();
    }
}
