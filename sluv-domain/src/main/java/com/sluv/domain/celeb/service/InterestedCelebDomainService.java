package com.sluv.domain.celeb.service;

import com.sluv.domain.celeb.entity.InterestedCeleb;
import com.sluv.domain.celeb.repository.InterestedCelebRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterestedCelebDomainService {

    private final InterestedCelebRepository interestedCelebRepository;

    @Transactional
    public void deleteAllByUserId(Long userId) {
        interestedCelebRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void saveAll(List<InterestedCeleb> interestedCelebList) {
        interestedCelebRepository.saveAll(interestedCelebList);
    }
}
