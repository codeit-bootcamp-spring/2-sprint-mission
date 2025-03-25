package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;

    @Builder
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
