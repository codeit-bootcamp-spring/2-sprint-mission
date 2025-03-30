package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UpdateUserParamDto(
        String nickname,
        UUID profileId
){
}
