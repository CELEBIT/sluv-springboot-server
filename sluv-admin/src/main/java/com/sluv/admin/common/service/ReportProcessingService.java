package com.sluv.admin.common.service;

import com.sluv.domain.alarm.enums.AlarmType;
import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.enums.UserStatus;
import com.sluv.domain.user.service.UserReportStackDomainService;
import com.sluv.infra.alarm.firebase.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportProcessingService {

    private final UserReportStackDomainService userReportStackDomainService;
    private final FcmNotificationService fcmNotificationService;

    public void processReport(User reportedUser, User reporterUser, String content, ReportStatus status) {
        if (status == ReportStatus.COMPLETE) {
            userReportStackDomainService.saveReportStack(reportedUser);

            fcmNotificationService.sendFCMNotification(reportedUser.getId(), "당신은 신고 당했습니다.", content, AlarmType.REPORT, null);
            fcmNotificationService.sendFCMNotification(reporterUser.getId(), "신고가 접수 되었습니다.", content, AlarmType.REPORT, null);

            blockReportedUser(reportedUser);
        } else {
            fcmNotificationService.sendFCMNotification(reporterUser.getId(), "신고가 기각되었습니다.", content, AlarmType.REPORT, null);
        }
    }

    private void blockReportedUser(User reportedUser) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        long reportCount = userReportStackDomainService.countByReportedAndCreatedAtAfter(reportedUser, oneMonthAgo);

        if (reportCount >= 3) {
            reportedUser.changeUserStatus(UserStatus.BLOCKED);
            fcmNotificationService.sendFCMNotification(reportedUser.getId(), "신고 누적으로 인한 계정정지 안내", "3회 이상 신고 누적으로 계정이 일시정지 됩니다.",
                    AlarmType.REPORT, null);
        }
    }
}
