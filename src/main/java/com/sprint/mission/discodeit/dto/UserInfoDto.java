package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserInfoDto {
    private UUID userid;
    private String username;
    private String email;
    private Boolean online;
    private UUID profileId;
}
