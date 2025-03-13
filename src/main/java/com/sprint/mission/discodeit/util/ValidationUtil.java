package com.sprint.mission.discodeit.util;

import java.util.UUID;


 //유효성 검사를 위한  유틸리티 클래스
 
public class ValidationUtil {
    //id 검증 로직
    public static void validateNotNull(UUID id, String fieldName) {
        if (id == null) {
            throw new IllegalArgumentException(fieldName + "는 null일 수 없습니다.");
        }
    }
    public static void validateNotEmpty(String str, String fieldName) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + "는 비어있을 수 없습니다.");
        }
    }
    //이메일 검증 로직
    public static void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }
    //객체 검증 로직
    public static void validateNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
} 