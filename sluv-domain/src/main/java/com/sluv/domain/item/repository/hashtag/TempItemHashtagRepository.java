package com.sluv.domain.item.repository.hashtag;

import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.hashtag.TempItemHashtag;
import com.sluv.domain.item.repository.hashtag.impl.TempItemHashtagRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempItemHashtagRepository extends JpaRepository<TempItemHashtag, Long>,
        TempItemHashtagRepositoryCustom {
    List<TempItemHashtag> findAllByTempItem(TempItem tempItem);

    void deleteAllByTempItemId(Long id);
}
