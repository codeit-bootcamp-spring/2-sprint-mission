package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateUserDTO {
    private String userName;
    private String email;
    private String password;
    private UUID profileId;

    public UpdateUserDTO(String userName, String email, String password, UUID profileId) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }
}