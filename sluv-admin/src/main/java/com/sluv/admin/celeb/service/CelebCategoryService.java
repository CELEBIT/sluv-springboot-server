package com.sluv.admin.celeb.service;

import com.sluv.admin.celeb.dto.CelebCategoryResponse;
import com.sluv.domain.celeb.service.CelebCategoryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CelebCategoryService {

    private final CelebCategoryDomainService celebCategoryDomainService;

    @Transactional(readOnly = true)
    public List<CelebCategoryResponse> findAllAvailableCategory() {
        return celebCategoryDomainService.findAllAvailableCategory().stream()
                .map(CelebCategoryResponse::from)
                .toList();
    }
}
