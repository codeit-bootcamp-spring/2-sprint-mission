package com.sprint.mission.discodeit.service.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChannelByIdResponse {
    private final UUID channelId;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final ChannelType type;
    private String name;
    private String description;
    private List<UUID> userIdList;
    private final Instant lastMessageTime;
}
