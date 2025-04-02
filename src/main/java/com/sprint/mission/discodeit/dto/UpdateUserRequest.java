package com.sprint.mission.discodeit.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String email;
    private String password;
}
