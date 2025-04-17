package com.sluv.admin.celeb.service;

import com.sluv.admin.celeb.dto.NewCelebChangeRequest;
import com.sluv.admin.celeb.dto.NewCelebPostDto;
import com.sluv.admin.celeb.dto.NewCelebSelfPostRequest;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.CelebCategory;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.service.*;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.TempItemDomainService;
import com.sluv.domain.question.service.QuestionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewCelebService {

    private final NewCelebDomainService newCelebDomainService;
    private final CelebDomainService celebDomainService;
    private final ItemDomainService itemDomainService;
    private final TempItemDomainService tempItemDomainService;
    private final RecentSelectCelebDomainService recentSelectCelebDomainService;
    private final QuestionDomainService questionDomainService;
    private final CelebCategoryDomainService celebCategoryDomainService;
    private final InterestedCelebDomainService interestedCelebDomainService;

    @Transactional(readOnly = true)
    public List<NewCelebPostDto> findAllNewCelebPostDto() {
        return newCelebDomainService.findAll().stream()
                .map(NewCelebPostDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public NewCelebPostDto findByNewCelebId(Long id) {
        NewCeleb newCeleb = newCelebDomainService.findById(id);
        return NewCelebPostDto.from(newCeleb);
    }

    @Transactional
    public void changeNewCelebToCeleb(NewCelebChangeRequest request) {
        Celeb celeb = celebDomainService.findById(request.getCelebId());
        //아이템의 뉴셀럽 변경
        itemDomainService.changeAllNewCelebToCeleb(celeb, request.getNewCelebId());
        //임시 아이템의 뉴셀럽 변경
        tempItemDomainService.changeAllNewCelebToCeleb(celeb, request.getNewCelebId());
        //최근선택 셀럽의 뉴셀럽 변경
        recentSelectCelebDomainService.changeAllNewCelebToCeleb(celeb, request.getNewCelebId());
        //커뮤니티의 뉴셀럽 변경
        questionDomainService.changeAllNewCelebToCeleb(celeb, request.getNewCelebId());
        //관심 뉴셀럽 변경
        interestedCelebDomainService.changeAllNewCelebToCeleb(celeb, request.getNewCelebId());
        //뉴셀럽 삭제
        newCelebDomainService.deleteById(request.getNewCelebId());

    }

    @Transactional
    public void registerNewCelebToCeleb(NewCelebSelfPostRequest request) {
        // 셀럽 생성
        Celeb parent = getCelebParent(request);
        CelebCategory celebCategory = celebCategoryDomainService.findById(request.getCategoryId());
        Celeb celeb = celebDomainService.saveCeleb(Celeb.of(request.getKrName(),
                request.getEnName(),
                celebCategory,
                parent)
        );
        //아이템의 뉴셀럽 변경
        itemDomainService.changeAllNewCelebToCeleb(celeb, request.getNewCelebId());
        //임시 아이템의 뉴셀럽 변경
        tempItemDomainService.changeAllNewCelebToCeleb(celeb, request.getNewCelebId());
        //최근선택 셀럽의 뉴셀럽 변경
        recentSelectCelebDomainService.changeAllNewCelebToCeleb(celeb, request.getNewCelebId());
        //커뮤니티의 뉴셀럽 변경
        questionDomainService.changeAllNewCelebToCeleb(celeb, request.getNewCelebId());
        //관심 뉴셀럽 변경
        interestedCelebDomainService.changeAllNewCelebToCeleb(celeb, request.getNewCelebId());
        //뉴셀럽 삭제
        newCelebDomainService.deleteById(request.getNewCelebId());
    }

    private Celeb getCelebParent(NewCelebSelfPostRequest request) {
        Celeb parent = null;
        if (request.getParentId() != null) {
            parent = celebDomainService.findById(request.getParentId());
        }
        return parent;
    }
}
