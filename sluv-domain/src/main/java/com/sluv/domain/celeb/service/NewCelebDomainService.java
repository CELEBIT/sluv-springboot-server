package com.sluv.domain.celeb.service;

import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.exception.NewCelebNotFoundException;
import com.sluv.domain.celeb.repository.NewCelebRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewCelebDomainService {

    private final NewCelebRepository newCelebRepository;

    public NewCeleb findByCelebNameOrNull(String newCelebName) {
        return newCelebRepository.findByCelebName(newCelebName).orElse(null);
    }

    public NewCeleb saveNewCelebByName(NewCeleb newCeleb) {
        return newCelebRepository.save(newCeleb);
    }

    public NewCeleb findByNewCelebIdOrNull(Long newCelebId) {
        return newCelebRepository.findById(newCelebId).orElseThrow(null);
    }

    public NewCeleb findById(Long newCelebId) {
        return newCelebRepository.findById(newCelebId).orElseThrow(NewCelebNotFoundException::new);
    }
}
