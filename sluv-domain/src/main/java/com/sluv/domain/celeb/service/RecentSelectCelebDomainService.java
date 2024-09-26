package com.sluv.domain.celeb.service;

import com.sluv.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.domain.celeb.repository.RecentSelectCelebRepository;
import com.sluv.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecentSelectCelebDomainService {

    private final RecentSelectCelebRepository recentSelectCelebRepository;

    @Transactional(readOnly = true)
    public List<RecentSelectCeleb> getRecentSelectCelebTop20(User user) {
        return recentSelectCelebRepository.getRecentSelectCelebTop20(user);
    }

    @Transactional
    public RecentSelectCeleb saveRecentSelectCeleb(RecentSelectCeleb recentSelectCeleb) {
        return recentSelectCelebRepository.save(recentSelectCeleb);
    }

    @Transactional
    public void deleteAllByUserId(Long userId) {
        recentSelectCelebRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void deleteByUserIdAndCelebId(Long userId, Long celebId) {
        recentSelectCelebRepository.deleteByUserIdAndCelebId(userId, celebId);
    }

    @Transactional
    public void deleteByUserIdAndNewCelebId(Long userId, Long celebId) {
        recentSelectCelebRepository.deleteByUserIdAndNewCelebId(userId, celebId);
    }

}
