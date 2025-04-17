package com.sluv.domain.item.service;

import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.repository.TempItemRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TempItemDomainService {

    private final TempItemRepository tempItemRepository;

    public TempItem findByIdOrNull(Long tempItemId) {
        return tempItemRepository.findById(tempItemId).orElse(null);
    }

    public Page<TempItem> getTempItemList(User user, Pageable pageable) {
        return tempItemRepository.getTempItemList(user, pageable);
    }

    public void deleteById(Long tempItemId) {
        tempItemRepository.deleteById(tempItemId);
    }

    public List<TempItem> findAllExceptLast(Long userId) {
        return tempItemRepository.findAllExceptLast(userId);
    }

    public Long countByUserId(Long userId) {
        return tempItemRepository.countByUserId(userId);
    }

    public TempItem saveTempItem(TempItem postTempItem) {
        return tempItemRepository.save(postTempItem);
    }

    public void changeAllNewBrandToBrand(Brand brand, Long newBrandId) {
        tempItemRepository.changeAllNewBrandToBrand(brand, newBrandId);
    }

    public void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId) {
        tempItemRepository.changeAllNewCelebToCeleb(celeb, newCelebId);
    }

    public void deleteAllByExpiredDate(int date) {
        tempItemRepository.changeItemStatusToDeletedByExpiredDate(date);
    }
}
