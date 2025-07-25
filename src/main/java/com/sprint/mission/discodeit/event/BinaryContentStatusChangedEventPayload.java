package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;

public record BinaryContentStatusChangedEventPayload(
        String userId,
        BinaryContentDto binaryContentDto
) {}