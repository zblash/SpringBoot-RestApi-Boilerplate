package com.springboot.restboilerplate.demo.utils;

import java.util.UUID;

public final class RandomStringGenerator {

    public static String generateId() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
