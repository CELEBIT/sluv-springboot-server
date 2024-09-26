package com.sluv.domain.search.service;

import com.sluv.domain.search.entity.RecentSearch;
import com.sluv.domain.search.repository.RecentSearchRepository;
import com.sluv.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecentSearchDomainService {

    private final RecentSearchRepository recentSearchRepository;

    @Transactional(readOnly = true)
    public List<RecentSearch> getRecentSearch(User user) {
        return recentSearchRepository.getRecentSearch(user);
    }

    @Transactional
    public void saveRecentSearch(User user, String keyword) {
        RecentSearch recentSearch = RecentSearch.of(user, keyword);
        recentSearchRepository.save(recentSearch);
    }

    @Transactional
    public void deleteByUserIdAndSearchWord(Long userId, String keyword) {
        recentSearchRepository.deleteByUserIdAndSearchWord(userId, keyword);
    }

    @Transactional
    public void deleteAllByUserId(Long userId) {
        recentSearchRepository.deleteAllByUserId(userId);
    }
}
