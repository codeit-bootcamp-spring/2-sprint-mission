package com.sprint.mission.discodeit.dto.data;

import java.util.UUID;

public record ChatMessageKafkaEvent(
    UUID channelId,
    UUID authorId
) {

}
