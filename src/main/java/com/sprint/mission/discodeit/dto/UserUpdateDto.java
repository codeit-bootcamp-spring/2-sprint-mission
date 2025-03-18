package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserUpdateDto {
    private UUID userid;
    private String username;
    private String email;
    private String password;
    private String profilePicturePath;
}
