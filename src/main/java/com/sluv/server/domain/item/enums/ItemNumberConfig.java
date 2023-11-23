package com.sluv.server.domain.item.enums;

import lombok.Getter;

@Getter
public enum ItemNumberConfig {
    이_아이템은_어때요_개수(4);

    private final int number;

    ItemNumberConfig(int number) {
        this.number = number;
    }
}
