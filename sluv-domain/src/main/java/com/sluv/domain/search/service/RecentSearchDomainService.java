package com.sluv.domain.search.service;

import com.sluv.domain.search.entity.RecentSearch;
import com.sluv.domain.search.repository.RecentSearchRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSearchDomainService {

    private final RecentSearchRepository recentSearchRepository;

    public List<RecentSearch> getRecentSearch(User user) {
        return recentSearchRepository.getRecentSearch(user);
    }

    public void saveRecentSearch(User user, String keyword) {
        RecentSearch recentSearch = RecentSearch.of(user, keyword);
        recentSearchRepository.save(recentSearch);
    }

    public void deleteByUserIdAndSearchWord(Long userId, String keyword) {
        recentSearchRepository.deleteByUserIdAndSearchWord(userId, keyword);
    }

    public void deleteAllByUserId(Long userId) {
        recentSearchRepository.deleteAllByUserId(userId);
    }
}
