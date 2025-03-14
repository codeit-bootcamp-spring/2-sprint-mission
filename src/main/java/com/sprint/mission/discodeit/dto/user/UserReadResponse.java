package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserReadResponse (
        UUID userId,
        String userName,
        String userEmail,
        UUID profileId,
        boolean online
){
}
