package com.sprint.mission.discodeit.dto.readStatus;

import java.util.UUID;

public record FindIsReadStatusDto(
    UUID channelUUID,
    String channelName
) {

}
