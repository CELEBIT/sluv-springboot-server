package com.sluv.domain.moderation.repository;

import com.sluv.domain.moderation.entity.ModerationJob;
import com.sluv.domain.moderation.repository.impl.ModerationJobRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModerationJobRepository extends JpaRepository<ModerationJob, Long>, ModerationJobRepositoryCustom {
}
