package com.sluv.server.domain.alarm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    ITEM("item"),
    QUESTION("question"),
    USER("user"),
    NOTICE("notice"),
    COMMENT("comment"),
    REPORT("report"),
    EDIT("edit");

    private final String name;
}
