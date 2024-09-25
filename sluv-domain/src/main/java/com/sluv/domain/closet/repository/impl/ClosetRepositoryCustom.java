package com.sluv.domain.closet.repository.impl;

import com.sluv.domain.closet.dto.ClosetCountDto;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClosetRepositoryCustom {
    List<Closet> getRecentAddCloset(Long itemId);

    Page<Closet> getUserAllCloset(Long userId, Pageable pageable);

    Page<Closet> getUserAllPublicCloset(Long userId, Pageable pageable);

    List<ClosetCountDto> getUserClosetList(User user);

    Boolean checkDuplicate(String name, Long closetId);

}
