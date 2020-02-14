package com.springboot.restboilerplate.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleType {
    ADMIN("ADMIN"),
    CUSTOMER("USER");

    private String value;

    RoleType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static RoleType fromValue(String text) {
        for (RoleType b : RoleType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

}