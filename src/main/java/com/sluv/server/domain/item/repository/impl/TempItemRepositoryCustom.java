package com.sluv.server.domain.item.repository.impl;

import com.sluv.server.domain.item.dto.TempItemResDto;
import com.sluv.server.domain.item.entity.TempItem;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TempItemRepositoryCustom {
    //    Page<TempItem> getTempItemList(User user, Pageable pageable);
    Page<TempItem> getTempItemList(User user, Pageable pageable);

    List<TempItem> findAllExceptLast(Long userId);

    List<TempItemResDto> getTempItemResDto(List<TempItem> content);
}
