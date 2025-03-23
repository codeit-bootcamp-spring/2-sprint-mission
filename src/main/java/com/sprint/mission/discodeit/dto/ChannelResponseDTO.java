package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ChannelResponseDTO {
    private UUID channelId;
    private String name;
    private String description;
    private ChannelType channelType;
    private Instant lastMessageTime;
    private List<UUID> userIds;

    public ChannelResponseDTO(UUID channelId,
                              String name,
                              String description,
                              ChannelType channelType,
                              Instant lastMessageTime,
                              List<UUID> userIds) {
        this.channelId = channelId;
        this.name = name;
        this.description = description;
        this.channelType = channelType;
        this.lastMessageTime = lastMessageTime;
        this.userIds = userIds;
    }
}
