package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.TempItem;
import com.sluv.server.domain.item.entity.TempItemLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempItemLinkRepository extends JpaRepository<TempItemLink, Long> {
    List<TempItemLink> findAllByTempItem(TempItem tempItem);

    void deleteAllByTempItemId(Long id);
}
