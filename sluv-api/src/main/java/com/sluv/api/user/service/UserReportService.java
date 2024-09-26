package com.sluv.api.user.service;

import com.sluv.api.user.dto.UserReportReqDto;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.exception.UserReportDuplicateException;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.domain.user.service.UserReportDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserReportService {

    private final UserDomainService userDomainService;
    private final UserReportDomainService userReportDomainService;

    @Transactional
    public void postUserReport(Long userId, Long targetId, UserReportReqDto dto) {
        // 피신고자 검색
        User user = userDomainService.findById(userId);
        User target = userDomainService.findById(targetId);

        // 신고 중복여부 확인
        boolean existence = userReportDomainService.findExistence(user, target);

        // 중복 신고라면 Exception 발생
        if (existence) {
            throw new UserReportDuplicateException();
        } else {
            // 중복이 아니라면 신고 접수
            userReportDomainService.saveUserReport(user, target, dto.getReason(), dto.getContent());
        }
    }
}
