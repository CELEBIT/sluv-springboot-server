package com.sluv.domain.celeb.service;

import com.sluv.domain.celeb.entity.CelebCategory;
import com.sluv.domain.celeb.exception.CelebCategoryNotFoundException;
import com.sluv.domain.celeb.repository.CelebCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CelebCategoryDomainService {

    private final CelebCategoryRepository celebCategoryRepository;

    public List<CelebCategory> findAllByParentIdIsNull() {
        return celebCategoryRepository.findAllByParentIdIsNull();
    }

    public List<CelebCategory> findAllAvailableCategory() {
        return celebCategoryRepository.findAllByParentIdIsNotNull();
    }

    public CelebCategory findById(Long categoryId) {
        return celebCategoryRepository.findById(categoryId).orElseThrow(CelebCategoryNotFoundException::new);
    }
}
