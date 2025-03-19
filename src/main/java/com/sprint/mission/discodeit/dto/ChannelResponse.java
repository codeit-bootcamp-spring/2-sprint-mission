package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
        UUID id,
        String name,
        Instant latestMessageTime,
        List<UUID> participantUserIds  // PRIVATE 채널의 참여한 사용자 ID 목록
) {}
