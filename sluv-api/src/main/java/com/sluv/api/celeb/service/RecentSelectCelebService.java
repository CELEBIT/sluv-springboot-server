package com.sluv.api.celeb.service;

import com.sluv.api.celeb.dto.request.RecentSelectCelebRequest;
import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.domain.celeb.service.CelebDomainService;
import com.sluv.domain.celeb.service.NewCelebDomainService;
import com.sluv.domain.celeb.service.RecentSelectCelebDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecentSelectCelebService {

    private final UserDomainService userDomainService;
    private final CelebDomainService celebDomainService;
    private final NewCelebDomainService newCelebDomainService;
    private final RecentSelectCelebDomainService recentSelectCelebDomainService;

    public void postRecentSelectCeleb(Long userId, RecentSelectCelebRequest dto) {
        User user = userDomainService.findById(userId);
        Celeb celeb = celebDomainService.findByIdOrNull(dto.getCelebId());
        NewCeleb newCeleb = newCelebDomainService.findByNewCelebIdOrNull(dto.getNewCelebId());

        recentSelectCelebDomainService.saveRecentSelectCeleb(RecentSelectCeleb.toEntity(user, celeb, newCeleb));

    }

    public void deleteAllRecentSelectCeleb(Long userId) {
        recentSelectCelebDomainService.deleteAllByUserId(userId);
    }

    public void deleteRecentSelectCeleb(Long userId, Long celebId, String flag) {
        if (flag.equals("Y")) {
            recentSelectCelebDomainService.deleteByUserIdAndCelebId(userId, celebId);
        } else {
            recentSelectCelebDomainService.deleteByUserIdAndNewCelebId(userId, celebId);
        }
    }

}
