package com.sprint.mission.discodeit.dto.controller.message;


import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UpdateMessageResponseDTO(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    List<FindBinaryContentResult> attachments,
    String content,
    UUID channelId,
    FindUserResult author
) {

}
