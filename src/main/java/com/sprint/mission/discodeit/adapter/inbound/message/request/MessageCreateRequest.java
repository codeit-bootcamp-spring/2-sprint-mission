package com.sprint.mission.discodeit.adapter.inbound.message.request;

import java.util.UUID;

public record MessageCreateRequest(
    UUID channelId,
    UUID authorId,
    String content
) {

}
