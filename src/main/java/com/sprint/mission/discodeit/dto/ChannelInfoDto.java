package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class ChannelInfoDto {
    private UUID channelId;
    private String channelName;
    private String description;
    private Instant lastMessageTime;
    private List<UUID> participantsUserIds;

    public ChannelInfoDto(UUID channelId, String channelName, String description, Instant lastMessageTime, List<UUID> participantsUserIds) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.description = description;
        this.lastMessageTime = lastMessageTime;
        this.participantsUserIds = participantsUserIds;
    }
}
