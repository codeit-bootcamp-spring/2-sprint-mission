package com.sprint.mission.discodeit.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateUserDTO {
    private final String userName;
    private final String email;
    private final String password;
    private final UUID ProfileId;

    public CreateUserDTO(String userName, String email, String password, UUID ProfileId) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.ProfileId = ProfileId;
    }
}
