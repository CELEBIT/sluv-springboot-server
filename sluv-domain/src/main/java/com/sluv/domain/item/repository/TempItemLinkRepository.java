package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.TempItemLink;
import com.sluv.domain.item.repository.impl.TempItemLinkRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempItemLinkRepository extends JpaRepository<TempItemLink, Long>, TempItemLinkRepositoryCustom {
    List<TempItemLink> findAllByTempItem(TempItem tempItem);

    void deleteAllByTempItemId(Long id);
}
