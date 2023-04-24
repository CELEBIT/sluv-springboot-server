package com.sluv.server.domain.celeb.service;

import com.sluv.server.domain.celeb.dto.RecentSelectCelebReqDto;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.server.domain.celeb.exception.CelebNotFoundException;
import com.sluv.server.domain.celeb.exception.NewCelebNotFoundException;
import com.sluv.server.domain.celeb.repository.CelebRepository;
import com.sluv.server.domain.celeb.repository.NewCelebRepository;
import com.sluv.server.domain.celeb.repository.RecentSelectCelebRepository;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecentSelectCelebService {
    private final CelebRepository celebRepository;
    private final NewCelebRepository newCelebRepository;
    private final RecentSelectCelebRepository recentSelectCelebRepository;

    public void postRecentSelectCeleb(User user, RecentSelectCelebReqDto dto){
        Celeb celeb = dto.getCelebId() != null
                ? celebRepository.findById(dto.getCelebId())
                                .orElseThrow(CelebNotFoundException::new)
                : null;

        NewCeleb newCeleb = dto.getNewCelebId() != null
                ? newCelebRepository.findById(dto.getNewCelebId())
                .orElseThrow(NewCelebNotFoundException::new)
                : null;

        recentSelectCelebRepository.save(
                RecentSelectCeleb.builder()
                        .celeb(celeb)
                        .newCeleb(newCeleb)
                        .user(user)
                        .build()
        );
    }

    @Transactional
    public void deleteAllRecentSelectCeleb(User user) {
        recentSelectCelebRepository.deleteAllByUserId(user.getId());
    }
}
