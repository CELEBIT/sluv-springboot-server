package com.sluv.domain.user.dto;

import com.sluv.domain.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportStackDto {

    private String nickname;
    private String profileImgUrl;
    private UserStatus userStatus;
    private Long waitingReportCount;
    private Long reportStackCount;

}
