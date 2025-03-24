package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChannelResponse {
    Channel channel;
    Instant lastMessageAt;
    List<UUID> userIds;
}
