package com.sluv.server.domain.user.scheduler;

import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.UserRepository;
import java.util.Calendar;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserScheduler {

    private final UserRepository userRepository;

    /**
     * SearchRank 업데이트
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 초 분 시 일 월 요일
    public void replaceDeletedUserData() {
        log.info("Deleted User Data Replace Time: {}", Calendar.getInstance().getTime());

        List<User> users = userRepository.getDeletedUsersAfter7Days();
        List<User> deletedUsers = users.stream().map(User::toDeletedUser).toList();
        userRepository.saveAll(deletedUsers);
    }
}
