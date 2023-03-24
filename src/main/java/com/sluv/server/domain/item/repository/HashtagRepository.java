package com.sluv.server.domain.item.repository;

import com.sluv.server.domain.item.entity.hashtag.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
