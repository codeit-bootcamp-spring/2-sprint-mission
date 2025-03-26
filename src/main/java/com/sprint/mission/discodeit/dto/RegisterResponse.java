package com.sprint.mission.discodeit.dto;

import lombok.Builder;

@Builder
public class RegisterResponse {
    private boolean success;
    private String message;
}
