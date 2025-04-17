package com.sprint.mission.discodeit.service.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record MessageResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID channelId,
        UserResponseDto author,
        List<BinaryContentResponseDto> attachments

) {

}