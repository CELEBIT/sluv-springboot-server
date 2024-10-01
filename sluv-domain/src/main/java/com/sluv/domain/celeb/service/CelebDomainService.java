package com.sluv.domain.celeb.service;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.CelebCategory;
import com.sluv.domain.celeb.exception.CelebNotFoundException;
import com.sluv.domain.celeb.repository.CelebRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CelebDomainService {

    private final CelebRepository celebRepository;

    public Page<Celeb> searchCeleb(String celebName, Pageable pageable) {
        return celebRepository.searchCeleb(celebName, pageable);
    }

    public List<Celeb> findTop10Celeb() {
        return celebRepository.findTop10Celeb();
    }

    public Celeb findByIdOrNull(Long celebId) {
        if (celebId == null) {
            return null;
        }
        return celebRepository.findById(celebId).orElse(null);
    }

    public List<Celeb> getCelebByCategory(CelebCategory category) {
        return celebRepository.getCelebByCategory(category);
    }

    public List<Celeb> searchInterestedCelebByParent(String celebName) {
        return celebRepository.searchInterestedCelebByParent(celebName);
    }

    public List<Celeb> searchInterestedCelebByChild(String celebName) {
        return celebRepository.searchInterestedCelebByChild(celebName);
    }

    public List<Celeb> findInterestedCeleb(User user) {
        return celebRepository.findInterestedCeleb(user);
    }

    public Celeb findById(Long celebId) {
        return celebRepository.findById(celebId).orElseThrow(CelebNotFoundException::new);
    }

    public List<Celeb> getCelebByContainKeyword(String keyword) {
        return celebRepository.getCelebByContainKeyword(keyword);
    }
}
