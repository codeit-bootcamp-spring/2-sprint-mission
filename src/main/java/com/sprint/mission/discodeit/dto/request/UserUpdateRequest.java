package com.sprint.mission.discodeit.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserUpdateRequest {
        private String newUsername;
        private String newEmail;
        private String newPassword;
}
