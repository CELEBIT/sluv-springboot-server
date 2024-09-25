package com.sluv.domain.user.enums;

public enum UserAge {
    UNKNOWN(-1),
    ZEROS(0),
    TEENAGERS(10),
    TWENTIES(20),
    THIRTIES(30),
    FORTIES(40),
    FIFTIES(50),
    SIXTIES(60),
    SEVENTIES(70),
    EIGHTIES(80),
    NINETIES(90);

    private final int startAge;

    UserAge(int startAge) {
        this.startAge = startAge;
    }

    public int getStartAge() {
        return startAge;
    }
}
