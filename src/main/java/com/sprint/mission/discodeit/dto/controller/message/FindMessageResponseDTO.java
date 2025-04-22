package com.sprint.mission.discodeit.dto.controller.message;

import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.util.HasCursor;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FindMessageResponseDTO(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    List<FindBinaryContentResult> attachments,
    String content,
    UUID channelId,
    FindUserResult author
) implements HasCursor {

  @Override
  public Object getCursor() {
    return createdAt;
  }
}
