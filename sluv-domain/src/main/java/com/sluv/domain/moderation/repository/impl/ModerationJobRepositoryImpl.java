package com.sluv.domain.moderation.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.domain.moderation.enums.ModerationJobStatus;
import com.sluv.domain.moderation.enums.ModerationTargetType;
import java.util.List;
import lombok.RequiredArgsConstructor;

import static com.sluv.domain.moderation.entity.QModerationJob.moderationJob;

@RequiredArgsConstructor
public class ModerationJobRepositoryImpl implements ModerationJobRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsOpenJob(ModerationTargetType targetType, Long targetId) {
        Integer result = jpaQueryFactory.selectOne()
                .from(moderationJob)
                .where(moderationJob.targetType.eq(targetType)
                        .and(moderationJob.targetId.eq(targetId))
                        .and(moderationJob.status.in(getOpenStatuses()))
                )
                .fetchFirst();

        return result != null;
    }

    private List<ModerationJobStatus> getOpenStatuses() {
        return List.of(
                ModerationJobStatus.REQUESTED,
                ModerationJobStatus.PROCESSING,
                ModerationJobStatus.NEEDS_REVIEW
        );
    }
}
