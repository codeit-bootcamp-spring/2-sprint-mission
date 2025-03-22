package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UpdateChannelParamDto(
        UUID channelUUID,
        String channelName
) {

}
