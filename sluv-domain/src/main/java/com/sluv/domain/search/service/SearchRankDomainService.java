package com.sluv.domain.search.service;

import com.sluv.domain.search.entity.SearchRank;
import com.sluv.domain.search.repository.SearchRankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchRankDomainService {

    private final SearchRankRepository searchRankRepository;

    @Transactional(readOnly = true)
    public List<SearchRank> findAllByOrderBySearchCountDesc() {
        return searchRankRepository.findAllByOrderBySearchCountDesc();
    }

    @Transactional
    public void deleteAllSearchRank() {
        searchRankRepository.deleteAll();
    }

    @Transactional
    public void saveAllSearchRank(List<SearchRank> result) {
        searchRankRepository.saveAll(result);
    }
}
