package com.sprint.mission.discodeit.dto.channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(UUID id, Instant createdAt, Instant latestMessageTime, List<UUID> memberIds) {}