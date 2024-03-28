package com.sluv.server.domain.user.dto;

import com.sluv.server.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTermsResDto {
    @Schema(description = "광고성 정보 수신 및 마케팅 활용 동의 상태")
    private Boolean termsStatus;

    public static UserTermsResDto of(User user) {
        return UserTermsResDto.builder()
                .termsStatus(user.getTermsStatus())
                .build();
    }
}
