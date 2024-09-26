package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.repository.TempItemRepository;
import com.sluv.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TempItemDomainService {

    private final TempItemRepository tempItemRepository;

    @Transactional(readOnly = true)
    public TempItem findByIdOrNull(Long tempItemId) {
        return tempItemRepository.findById(tempItemId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Page<TempItem> getTempItemList(User user, Pageable pageable) {
        return tempItemRepository.getTempItemList(user, pageable);
    }

    @Transactional
    public void deleteById(Long tempItemId) {
        tempItemRepository.deleteById(tempItemId);
    }

    @Transactional(readOnly = true)
    public List<TempItem> findAllExceptLast(Long userId) {
        return tempItemRepository.findAllExceptLast(userId);
    }

    @Transactional(readOnly = true)
    public Long countByUserId(Long userId) {
        return tempItemRepository.countByUserId(userId);
    }

    @Transactional
    public TempItem saveTempItem(TempItem postTempItem) {
        return tempItemRepository.save(postTempItem);
    }

}
