package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record ChannelUpdateParamDto(
        UUID channelUUID,
        String channelName
) {

}
