package com.sluv.api.domain.user.service;

import com.sluv.api.user.dto.UserReportReqDto;
import com.sluv.api.user.service.UserReportService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.domain.user.service.UserReportDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static com.sluv.api.fixture.UserFixture.구글_유저_생성;
import static com.sluv.api.fixture.UserFixture.카카오_유저_생성;
import static com.sluv.domain.user.enums.UserReportReason.BAD_MANNER;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserReportServiceTest {

    @InjectMocks
    private UserReportService userReportService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private UserReportDomainService userReportDomainService;

    @DisplayName("사용자를 신고한다.")
    @Test
    void postUserReportTest() {
        //given
        User user = 카카오_유저_생성().toBuilder().id(1L).build();
        User targetUser = 구글_유저_생성().toBuilder().id(2L).build();
        when(userDomainService.findById(eq(user.getId()))).thenReturn(user);
        when(userDomainService.findById(eq(targetUser.getId()))).thenReturn(targetUser);
        when(userReportDomainService.findExistence(eq(user), eq(targetUser))).thenReturn(false);

        UserReportReqDto reportReqDto = new UserReportReqDto(BAD_MANNER, "욕해요");

        //when
        userReportService.postUserReport(user.getId(), targetUser.getId(), reportReqDto);

        //then
        verify(userReportDomainService).saveUserReport(eq(user), eq(targetUser),
                eq(reportReqDto.getReason()), eq(reportReqDto.getContent()));
    }

}
