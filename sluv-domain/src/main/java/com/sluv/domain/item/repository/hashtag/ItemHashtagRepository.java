package com.sluv.domain.item.repository.hashtag;

import com.sluv.domain.item.entity.hashtag.ItemHashtag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemHashtagRepository extends JpaRepository<ItemHashtag, Long> {
    List<ItemHashtag> findAllByItemId(Long itemId);

    void deleteAllByItemId(Long itemId);
}
