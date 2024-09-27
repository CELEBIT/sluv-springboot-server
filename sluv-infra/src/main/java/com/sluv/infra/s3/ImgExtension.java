package com.sluv.infra.s3;

import lombok.Getter;

@Getter
public enum ImgExtension {
    JPG("jpeg"),
    JPEG("jpeg"),
    PNG("png");

    ImgExtension(String uploadExtension) {
        this.uploadExtension = uploadExtension;
    }

    private final String uploadExtension;
}
