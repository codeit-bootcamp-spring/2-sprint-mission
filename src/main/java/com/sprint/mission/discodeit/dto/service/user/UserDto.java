package com.sprint.mission.discodeit.dto.service.user;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDto;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    boolean online
) {

}
