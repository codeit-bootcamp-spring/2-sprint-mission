package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ChannelResponseDto {
    private UUID id;
    private ChannelType type;
    private String name;
    private String description;
    private Instant latestMessageAt;
    private List<UUID> userIds;

    public ChannelResponseDto(UUID id, ChannelType type, String name, String description, Instant latestMessageAt, List<UUID> userIds) {
       this.id = id;
       this.type = type;
       this.name = name;
       this.description = description;
       this.latestMessageAt = latestMessageAt;
       this.userIds = userIds;
    }
}
