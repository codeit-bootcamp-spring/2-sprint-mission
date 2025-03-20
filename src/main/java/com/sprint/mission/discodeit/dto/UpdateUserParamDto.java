package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UpdateUserParamDto(
        UUID userUUID,
        String nickname,
        UUID profileId
){
}
