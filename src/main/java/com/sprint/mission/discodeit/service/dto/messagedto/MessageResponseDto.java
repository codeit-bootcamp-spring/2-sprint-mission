package com.sprint.mission.discodeit.service.dto.messagedto;

import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.dto.userdto.UserResponseDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID channelId,
        UserResponseDto author,
        List<BinaryContentResponseDto> attachmentIds

) {

}