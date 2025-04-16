package com.sprint.mission.discodeit.service.dto.userdto;

import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentResponseDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponseDto(
        UUID id,
        String username,
        String email,
        BinaryContentResponseDto profileId,
        Boolean online
) {

}
