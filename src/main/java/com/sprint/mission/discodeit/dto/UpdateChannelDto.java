package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UpdateChannelDto(
        UUID channelKey,
        String newName,
        String newIntroduction
) {
}
