package com.sluv.admin.celeb.service;

import com.sluv.admin.celeb.dto.CelebPageResponse;
import com.sluv.admin.celeb.dto.CelebResponse;
import com.sluv.admin.celeb.dto.CelebSelfPostRequest;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.CelebCategory;
import com.sluv.domain.celeb.service.CelebCategoryDomainService;
import com.sluv.domain.celeb.service.CelebDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CelebService {

    private final CelebDomainService celebDomainService;
    private final CelebCategoryDomainService celebCategoryDomainService;

    @Transactional(readOnly = true)
    public CelebPageResponse findAllCelebResponsePage(String keyword, Integer page) {
        Pageable pageable = getPageable(page);
        Page<Celeb> celebPage = getCelebPage(keyword, pageable);
        List<CelebResponse> content = celebPage.stream().map(CelebResponse::from).toList();

        return CelebPageResponse.of(celebPage.getNumber(), celebPage.getTotalPages(), content);
    }

    private Page<Celeb> getCelebPage(String keyword, Pageable pageable) {
        Page<Celeb> celebPage;
        if (keyword != null) {
            celebPage = celebDomainService.getCelebByContainKeyword(keyword, pageable);
        } else {
            celebPage = celebDomainService.findAllWithPageable(pageable);
        }
        return celebPage;
    }

    private Pageable getPageable(Integer page) {
        if (page == null) {
            page = 1;
        }
        return PageRequest.of(page - 1, 20);
    }

    @Transactional
    public void saveCeleb(CelebSelfPostRequest request) {
        Celeb parent = request.getParentId() != null ? celebDomainService.findById(request.getParentId()) : null;
        CelebCategory celebCategory = celebCategoryDomainService.findById(request.getCategoryId());
        celebDomainService.saveCeleb(
                Celeb.of(request.getKrName(), request.getEnName(), celebCategory, parent)
        );
    }
}
