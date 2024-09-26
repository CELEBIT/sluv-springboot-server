package com.sluv.batch.user;

import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

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
