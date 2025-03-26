package com.sprint.mission.discodeit.dto;

import lombok.Builder;

@Builder
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
}
