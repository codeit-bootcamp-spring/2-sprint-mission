package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCreateRequest {
    private String username;
    private String email;
    private String password;

    @Builder
    public UserCreateRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
