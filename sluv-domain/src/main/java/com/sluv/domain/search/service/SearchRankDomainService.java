package com.sluv.domain.search.service;

import com.sluv.domain.search.entity.SearchRank;
import com.sluv.domain.search.repository.SearchRankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchRankDomainService {

    private final SearchRankRepository searchRankRepository;

    public List<SearchRank> findAllByOrderBySearchCountDesc() {
        return searchRankRepository.findAllByOrderBySearchCountDesc();
    }

    public void deleteAllSearchRank() {
        searchRankRepository.deleteAll();
    }

    public void saveAllSearchRank(List<SearchRank> result) {
        searchRankRepository.saveAll(result);
    }
}
