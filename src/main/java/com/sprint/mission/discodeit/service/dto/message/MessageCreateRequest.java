package com.sprint.mission.discodeit.service.dto.message;


import java.util.UUID;

public record MessageCreateRequest(
    String content,
    UUID channelId,
    UUID authorId
) {

}