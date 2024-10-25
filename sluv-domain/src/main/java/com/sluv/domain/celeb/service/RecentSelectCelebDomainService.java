package com.sluv.domain.celeb.service;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.domain.celeb.repository.RecentSelectCelebRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSelectCelebDomainService {

    private final RecentSelectCelebRepository recentSelectCelebRepository;

    public List<RecentSelectCeleb> getRecentSelectCelebTop20(User user) {
        return recentSelectCelebRepository.getRecentSelectCelebTop20(user);
    }

    public RecentSelectCeleb saveRecentSelectCeleb(RecentSelectCeleb recentSelectCeleb) {
        return recentSelectCelebRepository.save(recentSelectCeleb);
    }

    public void deleteAllByUserId(Long userId) {
        recentSelectCelebRepository.deleteAllByUserId(userId);
    }

    public void deleteByUserIdAndCelebId(Long userId, Long celebId) {
        recentSelectCelebRepository.deleteByUserIdAndCelebId(userId, celebId);
    }

    public void deleteByUserIdAndNewCelebId(Long userId, Long celebId) {
        recentSelectCelebRepository.deleteByUserIdAndNewCelebId(userId, celebId);
    }

    public void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId) {
        recentSelectCelebRepository.changeAllNewCelebToCeleb(celeb, newCelebId);
    }
}
