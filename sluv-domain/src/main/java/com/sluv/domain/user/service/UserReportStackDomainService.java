package com.sluv.domain.user.service;

import com.sluv.domain.user.repository.UserReportStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserReportStackDomainService {

    private final UserReportStackRepository userReportStackRepository;

    public void deleteAllByReportedId(Long userId) {
        userReportStackRepository.deleteAllByReportedId(userId);
    }

}
