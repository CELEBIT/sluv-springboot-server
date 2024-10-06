package com.sluv.admin.user.dto;

import com.sluv.domain.user.dto.UserReportStackDto;
import com.sluv.domain.user.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminInfoDto {

    private String nickname;
    private String profileImgUrl;
    private UserStatus userStatus;
    @Schema(description = "처리 대기 중인 신고 수")
    private Long waitingReportCount;
    @Schema(description = "승인된 누적 신고 수")
    private Long reportStackCount;

    public static UserAdminInfoDto from(UserReportStackDto dto) {
        return UserAdminInfoDto.builder()
                .nickname(dto.getNickname())
                .profileImgUrl(dto.getProfileImgUrl())
                .userStatus(dto.getUserStatus())
                .waitingReportCount(dto.getWaitingReportCount())
                .reportStackCount(dto.getReportStackCount())
                .build();
    }
}
