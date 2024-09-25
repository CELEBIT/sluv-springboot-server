package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.TempItemImg;
import com.sluv.domain.item.repository.impl.TempItemImgRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempItemImgRepository extends JpaRepository<TempItemImg, Long>, TempItemImgRepositoryCustom {
    List<TempItemImg> findAllByTempItem(TempItem tempItem);

    void deleteAllByTempItemId(Long id);
}
