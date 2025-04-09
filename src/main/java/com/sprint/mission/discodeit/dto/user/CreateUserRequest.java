package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserRequest {

    private String username;
    private String email;
    private String password;
}
