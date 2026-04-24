package com.sluv.domain.moderation.service;

import com.sluv.domain.moderation.entity.ModerationJob;
import com.sluv.domain.moderation.enums.ModerationTargetType;
import com.sluv.domain.moderation.repository.ModerationJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModerationJobDomainService {

    private final ModerationJobRepository moderationJobRepository;

    public void createQuestionJobIfAbsent(Long questionId, Long requestedBy) {
        if (moderationJobRepository.existsOpenJob(ModerationTargetType.QUESTION, questionId)) {
            return;
        }

        moderationJobRepository.save(ModerationJob.createQuestionJob(questionId, requestedBy));
    }
}
