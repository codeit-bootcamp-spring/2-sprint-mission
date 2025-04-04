package com.sprint.mission.discodeit.adapter.inbound.message.request;

import java.util.UUID;

public record MessageCreateRequest(
    String content,
    UUID channelId,
    UUID authorId
) {

}
