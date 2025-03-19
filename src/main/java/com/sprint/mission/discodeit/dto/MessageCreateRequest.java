package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
   String content,
   UUID senderId,
   UUID channelId,
   List<BinaryContentCreateRequest> attachments
) {}
