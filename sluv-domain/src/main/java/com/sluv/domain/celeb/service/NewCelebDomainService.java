package com.sluv.domain.celeb.service;

import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.exception.NewCelebNotFoundException;
import com.sluv.domain.celeb.repository.NewCelebRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (newCelebId == null) {
            return null;
        }
        return newCelebRepository.findById(newCelebId).orElseThrow(null);
    }

    public NewCeleb findById(Long newCelebId) {
        return newCelebRepository.findById(newCelebId).orElseThrow(NewCelebNotFoundException::new);
    }

    public List<NewCeleb> findAll() {
        return newCelebRepository.findAllByOrderByCreatedAtDesc();
    }


    public void deleteById(Long newCelebId) {
        newCelebRepository.deleteById(newCelebId);
    }
}
