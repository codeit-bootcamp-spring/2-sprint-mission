package com.sprint.mission.discodeit.dto.user.response;

import java.util.UUID;

public class UserResponse {
    private boolean success;
    private UUID userId;   // 유저 ID 추가
    private String username;
    private String email;
    private String message;

    public UserResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public UserResponse(boolean success, UUID userId, String username, String email) {
        this.success = success;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

}