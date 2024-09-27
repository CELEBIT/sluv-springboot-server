package com.sluv.domain.item.repository.impl;

import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TempItemRepositoryCustom {
    //    Page<TempItem> getTempItemList(User user, Pageable pageable);
    Page<TempItem> getTempItemList(User user, Pageable pageable);

    List<TempItem> findAllExceptLast(Long userId);

}
