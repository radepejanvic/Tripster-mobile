package com.example.tripster;

import java.util.Random;

public enum UserType {
    GUEST, HOST, ADMIN;

    private static final Random PRNG = new Random();

    public static UserType randomUserType() {
        UserType[] userTypes = values();
        return userTypes[PRNG.nextInt(userTypes.length)];
    }
}
