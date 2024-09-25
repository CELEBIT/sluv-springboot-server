package com.sluv.domain.celeb.service;

import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.exception.NewCelebNotFoundException;
import com.sluv.domain.celeb.repository.NewCelebRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewCelebDomainService {

    private final NewCelebRepository newCelebRepository;

    @Transactional(readOnly = true)
    public NewCeleb findByCelebNameOrNull(String newCelebName) {
        return newCelebRepository.findByCelebName(newCelebName).orElse(null);
    }

    @Transactional
    public NewCeleb saveNewCelebByName(NewCeleb newCeleb) {
        return newCelebRepository.save(newCeleb);
    }

    @Transactional(readOnly = true)
    public NewCeleb findByNewCelebIdOrNull(Long newCelebId) {
        return newCelebRepository.findById(newCelebId).orElseThrow(null);
    }

    @Transactional(readOnly = true)
    public NewCeleb findById(Long newCelebId) {
        return newCelebRepository.findById(newCelebId).orElseThrow(NewCelebNotFoundException::new);
    }
}
