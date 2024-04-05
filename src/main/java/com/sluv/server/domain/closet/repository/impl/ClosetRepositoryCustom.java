package com.sluv.server.domain.closet.repository.impl;

import com.sluv.server.domain.closet.dto.ClosetResDto;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClosetRepositoryCustom {
    List<Closet> getRecentAddCloset(Long itemId);

    Page<Closet> getUserAllCloset(Long userId, Pageable pageable);

    Page<Closet> getUserAllPublicCloset(Long userId, Pageable pageable);

    List<ClosetResDto> getUserClosetList(User user);

    Boolean checkDuplicate(String name, Long closetId);

    void withdrawAllByUserId(Long userId);
}
