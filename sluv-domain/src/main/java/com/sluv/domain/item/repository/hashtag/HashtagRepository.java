package com.sluv.domain.item.repository.hashtag;

import com.sluv.domain.item.entity.hashtag.Hashtag;
import com.sluv.domain.item.repository.hashtag.impl.HashtagRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long>, HashtagRepositoryCustom {

    Optional<Hashtag> findByContent(String hashtagContent);
}
