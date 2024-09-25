package com.sluv.domain.auth.enums;

import com.sluv.domain.auth.exception.NoSupportSocialTypeException;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "SnsType")
public enum SnsType {
    KAKAO,
    GOOGLE,
    APPLE,
    ETC;

    public static SnsType fromString(String text) {
        for (SnsType snsType : SnsType.values()) {
            if (snsType.name().equalsIgnoreCase(text)) {
                return snsType;
            }
        }
        throw new NoSupportSocialTypeException();
    }
}
