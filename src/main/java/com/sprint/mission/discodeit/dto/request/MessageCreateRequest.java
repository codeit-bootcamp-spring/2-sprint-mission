package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Message 생성 정보")
public record MessageCreateRequest(
    String content,
    UUID channelId,
    UUID authorId
) {

}
