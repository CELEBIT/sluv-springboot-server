package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.TempItem;
import com.sluv.server.domain.item.entity.TempItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempItemImgRepository extends JpaRepository<TempItemImg, Long> {
    List<TempItemImg> findAllByTempItem(TempItem tempItem);

    void deleteAllByTempItemId(Long id);
}
