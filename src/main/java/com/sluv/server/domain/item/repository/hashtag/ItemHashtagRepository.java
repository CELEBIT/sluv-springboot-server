package com.sluv.server.domain.item.repository.hashtag;

import com.sluv.server.domain.item.entity.hashtag.ItemHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemHashtagRepository extends JpaRepository<ItemHashtag, Long> {
    List<ItemHashtag> findAllByItemId(Long itemId);
}
