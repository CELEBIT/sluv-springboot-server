package com.sluv.domain.user.service;

import com.sluv.domain.user.repository.UserReportStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserReportStackDomainService {

    private final UserReportStackRepository userReportStackRepository;

    @Transactional
    public void deleteAllByReportedId(Long userId) {
        userReportStackRepository.deleteAllByReportedId(userId);
    }

}
