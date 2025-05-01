package com.sluv.domain.celeb.service;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.InterestedCeleb;
import com.sluv.domain.celeb.repository.InterestedCelebRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestedCelebDomainService {

    private final InterestedCelebRepository interestedCelebRepository;

    public void deleteAllByUserId(Long userId) {
        interestedCelebRepository.deleteAllByUserId(userId);
    }

    public void saveAll(List<InterestedCeleb> interestedCelebList) {
        interestedCelebRepository.saveAll(interestedCelebList);
    }

    public List<InterestedCeleb> findInterestedCelebByUser(User user) {
        return interestedCelebRepository.findAllByUserId(user.getId());
    }

    public void changeAllNewCelebToCeleb(Celeb celeb, Long newCelebId) {
        interestedCelebRepository.changeAllNewCelebToCeleb(celeb, newCelebId);
    }
}
