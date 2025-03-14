package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserReadResponse (
        String userName,
        String userEmail,
        UUID profileId,
        boolean online
){
}
