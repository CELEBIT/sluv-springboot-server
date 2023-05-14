package com.sluv.server.global.s3;

import lombok.Getter;

@Getter
public enum ImgExtension {
    JPG("jpeg"),
    JPEG("jpg"),
    PNG("png");

    ImgExtension(String uploadExtension) {
        this.uploadExtension = uploadExtension;
    }

    private final String uploadExtension;
}
