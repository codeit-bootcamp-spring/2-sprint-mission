package com.sprint.mission.discodeit.dto.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(UUID id, Instant createdAt, String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {}