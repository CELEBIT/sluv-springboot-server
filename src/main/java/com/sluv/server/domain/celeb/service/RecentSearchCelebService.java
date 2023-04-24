package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.brand.dto.RecentBrandReqDto;
import com.sluv.server.domain.brand.entity.Brand;
import com.sluv.server.domain.brand.entity.NewBrand;
import com.sluv.server.domain.brand.entity.RecentBrand;
import com.sluv.server.domain.brand.exception.BrandNotFoundException;
import com.sluv.server.domain.brand.exception.NewBrandNotFoundException;
import com.sluv.server.domain.brand.repository.BrandRepository;
import com.sluv.server.domain.brand.repository.NewBrandRepository;
import com.sluv.server.domain.brand.repository.RecentBrandRepository;
import com.sluv.server.domain.celeb.dto.RecentSearchCelebReqDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.entity.RecentSearchCeleb;
import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.celeb.exception.NewCelebNotFoundException;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import com.sluv.server.domain.celeb.repository.RecentSearchCelebRepository;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecentSearchCelebService {
    private final CelebRepository celebRepository;
    private final NewCelebRepository newCelebRepository;
    private final RecentSearchCelebRepository recentSearchCelebRepository;

    public void postRecentSearchCeleb(User user, RecentSearchCelebReqDto dto){
        Celeb celeb = dto.getCelebId() != null
                ? celebRepository.findById(dto.getCelebId())
                                .orElseThrow(CelebNotFoundException::new)
                : null;

        NewCeleb newCeleb = dto.getNewCelebId() != null
                ? newCelebRepository.findById(dto.getNewCelebId())
                .orElseThrow(NewCelebNotFoundException::new)
                : null;

        recentSearchCelebRepository.save(
                RecentSearchCeleb.builder()
                        .celeb(celeb)
                        .newCeleb(newCeleb)
                        .user(user)
                        .build()
        );
    }
}
