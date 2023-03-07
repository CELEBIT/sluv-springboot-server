package com.sluv.server.domain.auth.enums;

import com.sluv.server.domain.auth.exception.NoSupportSocialTypeException;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public enum SnsType {
    KAKAO,
    GOOGLE,
    APPLE;

    public static SnsType fromString(String text) {
        for (SnsType snsType : SnsType.values()) {
            if (snsType.name().equalsIgnoreCase(text)) {
                return snsType;
            }
        }
        throw new NoSupportSocialTypeException();
    }
}
