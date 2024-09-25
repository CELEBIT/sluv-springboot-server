package com.sluv.domain.celeb.service;

import com.sluv.domain.celeb.entity.CelebCategory;
import com.sluv.domain.celeb.repository.CelebCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CelebCategoryDomainService {

    private final CelebCategoryRepository celebCategoryRepository;

    @Transactional(readOnly = true)
    public List<CelebCategory> findAllByParentIdIsNull() {
        return celebCategoryRepository.findAllByParentIdIsNull();
    }

}
