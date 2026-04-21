package com.sluv.domain.moderation.repository.impl;

import com.sluv.domain.moderation.enums.ModerationTargetType;

public interface ModerationJobRepositoryCustom {
    boolean existsOpenJob(ModerationTargetType targetType, Long targetId);
}
