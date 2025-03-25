package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatusType;
import lombok.Data;

import java.util.UUID;

@Data
public class UserInfoDto {
    private UUID userid;
    private String username;
    private String email;
    private UserStatusType status;
}
