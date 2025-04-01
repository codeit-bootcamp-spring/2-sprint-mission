package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;
import lombok.Getter;

@Getter
public class UserCreateRequestDto {
        private String username;
        private String email;
        private String password;
        private UUID profileId;
}
