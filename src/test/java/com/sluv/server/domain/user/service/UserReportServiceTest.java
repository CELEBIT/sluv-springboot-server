package com.sluv.server.domain.user.service;

import static com.sluv.server.domain.user.enums.UserReportReason.BAD_MANNER;
import static com.sluv.server.fixture.UserFixture.구글_유저_생성;
import static com.sluv.server.fixture.UserFixture.카카오_유저_생성;
import static org.assertj.core.api.Assertions.assertThat;

import com.sluv.server.domain.user.dto.UserReportReqDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.UserReportRepository;
import com.sluv.server.domain.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserReportServiceTest {

    @Autowired
    private UserReportService userReportService;

    @Autowired
    private UserReportRepository userReportRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clear() {
        userReportRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("사용자를 신고한다.")
    @Test
    void postUserReportTest() {
        //given
        User user1 = 카카오_유저_생성();
        User user2 = 구글_유저_생성();
        userRepository.saveAll(List.of(user1, user2));

        UserReportReqDto reportReqDto = new UserReportReqDto(BAD_MANNER, "욕해요");

        //when
        userReportService.postUserReport(user1, user2.getId(), reportReqDto);

        //then
        assertThat(userReportRepository.findAll()).hasSize(1);
    }

}
