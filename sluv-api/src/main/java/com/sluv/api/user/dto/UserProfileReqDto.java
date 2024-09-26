package com.sluv.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileReqDto {
    @Schema(description = "User의 Profile Img Url")
    private String imgUrl;
    @Schema(description = "User의 NickName")
    @Size(max = 30) // 최대 15글자 까지 가능.
    private String nickName;
}
