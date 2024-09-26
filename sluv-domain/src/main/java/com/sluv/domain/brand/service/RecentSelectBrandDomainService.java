package com.sluv.domain.brand.service;

import com.sluv.domain.brand.entity.RecentSelectBrand;
import com.sluv.domain.brand.repository.RecentSelectBrandRepository;
import com.sluv.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecentSelectBrandDomainService {

    private final RecentSelectBrandRepository recentSelectBrandRepository;

    @Transactional
    public RecentSelectBrand saveRecentSelectBrand(RecentSelectBrand recentSelectBrand) {
        return recentSelectBrandRepository.save(recentSelectBrand);
    }

    @Transactional
    public void deleteAllByUserId(Long userId) {
        recentSelectBrandRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void deleteByUserIdAndBrandId(Long userId, Long brandId) {
        recentSelectBrandRepository.deleteByUserIdAndBrandId(userId, brandId);
    }

    @Transactional
    public void deleteByUserIdAndNewBrandId(Long userId, Long brandId) {
        recentSelectBrandRepository.deleteByUserIdAndNewBrandId(userId, brandId);
    }

    @Transactional(readOnly = true)
    public List<RecentSelectBrand> getRecentSelectBrandTop20(User user) {
        return recentSelectBrandRepository.getRecentSelectBrandTop20(user);
    }

}
