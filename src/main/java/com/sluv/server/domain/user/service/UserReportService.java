package com.sluv.server.domain.user.service;

import com.sluv.server.domain.user.dto.UserReportReqDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.entity.UserReport;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.exception.UserReportDuplicateException;
import com.sluv.server.domain.user.repository.UserReportRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserReportService {
    private final UserRepository userRepository;
    private final UserReportRepository userReportRepository;

    public void postUserReport(User user, Long userId, UserReportReqDto dto) {
        // 피신고자 검색
        User target = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 신고 중복여부 확인
        boolean existence = userReportRepository.findExistence(user, target);

        // 중복 신고라면 Exception 발생
        if (existence) {
            throw new UserReportDuplicateException();
        } else {
            // 중복이 아니라면 신고 접수
            userReportRepository.save(
                    UserReport.toEntity(user, target, dto)
            );
        }
    }
}
