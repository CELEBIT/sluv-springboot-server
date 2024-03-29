package com.sluv.server.domain.item.repository.hashtag;

import com.sluv.server.domain.item.entity.hashtag.Hashtag;
import com.sluv.server.domain.item.repository.hashtag.impl.HashtagRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long>, HashtagRepositoryCustom {

    Optional<Hashtag> findByContent(String hashtagContent);
}
