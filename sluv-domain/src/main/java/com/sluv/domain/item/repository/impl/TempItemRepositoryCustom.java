package com.sluv.domain.item.repository.impl;

import com.sluv.domain.brand.entity.Brand;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TempItemRepositoryCustom {
    //    Page<TempItem> getTempItemList(User user, Pageable pageable);
    Page<TempItem> getTempItemList(User user, Pageable pageable);

    List<TempItem> findAllExceptLast(Long userId);

    void changeAllNewBrandToBrand(Brand brand, Long newBrandId);

    void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId);

    void changeItemStatusToDeletedByExpiredDate(int date);
}
