package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UpdateUserDto(
        UUID userUUID,
        String nickname,
        byte[] imageFile
){
}
