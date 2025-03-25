package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatusType;
import lombok.Data;

@Data
public class CreateUserDto {
    private String username;
    private String email;
    private String password;
    private String profilePicturePath;
    private UserStatusType status;
}
