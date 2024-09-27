package com.sluv.api.user.dto;

import com.sluv.domain.user.enums.UserWithdrawReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithdrawReqDto {
    @Schema(description = "탈퇴 이유")
    private UserWithdrawReason reason;
    @Schema(description = "탈퇴 내용")
    private String content;
}
