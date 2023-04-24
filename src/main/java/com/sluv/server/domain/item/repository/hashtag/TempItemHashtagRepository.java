package com.sluv.server.domain.item.repository.hashtag;

import com.sluv.server.domain.item.entity.TempItem;
import com.sluv.server.domain.item.entity.hashtag.TempItemHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempItemHashtagRepository extends JpaRepository<TempItemHashtag, Long> {
    List<TempItemHashtag> findAllByTempItem(TempItem tempItem);

    void deleteAllByTempItemId(Long id);
}
