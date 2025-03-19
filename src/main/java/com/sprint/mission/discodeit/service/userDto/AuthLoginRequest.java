package com.sprint.mission.discodeit.service.userDto;

import java.util.UUID;

public record AuthLoginRequest (
        UUID userId,
        String username,
        String password){

}

