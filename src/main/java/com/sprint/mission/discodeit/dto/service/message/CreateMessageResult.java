package com.sprint.mission.discodeit.dto.service.message;

import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreateMessageResult(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    List<FindBinaryContentResult> attachments,
    String content,
    UUID channelId,
    FindUserResult author
) {

}
