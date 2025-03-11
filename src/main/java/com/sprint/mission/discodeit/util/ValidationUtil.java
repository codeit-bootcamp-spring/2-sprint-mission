package com.sprint.mission.discodeit.util;

import java.util.UUID;

public class ValidationUtil {

    public static void validateNotNull(UUID id, String fieldName) {
        if (id == null) {
            throw new IllegalArgumentException(fieldName + "는 null일 수 없습니다.");
        }
    }

    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + "는 비어있을 수 없습니다.");
        }
    }

    public static void validateNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}