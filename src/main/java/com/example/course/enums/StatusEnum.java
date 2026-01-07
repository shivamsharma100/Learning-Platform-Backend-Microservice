package com.example.course.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum StatusEnum {
    AVAILABLE("available"),
    NOT_AVAILABLE("notAvailable");

    private final String messageType;

    StatusEnum(String messageType) {
        this.messageType = messageType;
    }
    @JsonValue
    public String getMessageType() {
        return this.messageType;
    }

    @JsonCreator
    public static StatusEnum findByName(String name) {
        return Arrays.stream(values())
                .filter(notificationType -> notificationType.getMessageType().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unrecognized status value : " + name));
    }


}
