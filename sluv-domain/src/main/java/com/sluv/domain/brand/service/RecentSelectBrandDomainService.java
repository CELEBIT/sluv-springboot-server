package com.sluv.domain.brand.service;

import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.brand.entity.RecentSelectBrand;
import com.sluv.domain.brand.repository.RecentSelectBrandRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSelectBrandDomainService {

    private final RecentSelectBrandRepository recentSelectBrandRepository;

    public RecentSelectBrand saveRecentSelectBrand(RecentSelectBrand recentSelectBrand) {
        return recentSelectBrandRepository.save(recentSelectBrand);
    }

    public void deleteAllByUserId(Long userId) {
        recentSelectBrandRepository.deleteAllByUserId(userId);
    }

    public void deleteByUserIdAndBrandId(Long userId, Long brandId) {
        recentSelectBrandRepository.deleteByUserIdAndBrandId(userId, brandId);
    }

    public void deleteByUserIdAndNewBrandId(Long userId, Long brandId) {
        recentSelectBrandRepository.deleteByUserIdAndNewBrandId(userId, brandId);
    }

    public List<RecentSelectBrand> getRecentSelectBrandTop20(User user) {
        return recentSelectBrandRepository.getRecentSelectBrandTop20(user);
    }

    public void changeAllNewBrandToBrand(Brand brand, Long newBrandId) {
        recentSelectBrandRepository.changeAllNewBrandToBrand(brand, newBrandId);
    }
}
