package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserResponseDto {
    private final UUID id;
    private final String username;
    private final String email;
    private final UUID profiledId;
    private final Boolean online;

    public UserResponseDto(User user,UserStatus status) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profiledId = user.getProfileId();
        this.online = status != null && status.isOnline();
    }
}
