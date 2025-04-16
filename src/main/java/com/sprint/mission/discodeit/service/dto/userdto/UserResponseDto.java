package com.sprint.mission.discodeit.service.dto.userdto;

import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentResponseDto;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String email,
        BinaryContentResponseDto profileId,
        Boolean online
) {

}
