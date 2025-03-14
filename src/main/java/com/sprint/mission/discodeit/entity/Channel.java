package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID channelId;
    private final UUID serverId;
    private final UUID creatorId;
    private String name;
    private ChannelType type;

    public final Instant createdAt;
    public Instant updatedAt;

    public Channel(UUID serverId, UUID creatorId, String name, ChannelType type) {
        this(UUID.randomUUID(), serverId, creatorId,Instant.now(), name, type);
    }

    public Channel(UUID channelId, UUID serverId, UUID creatorId, Instant createdAt, String name,ChannelType type) {
        this.channelId = channelId;
        this.serverId = serverId;
        this.creatorId = creatorId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.name = name;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
        updatedAt = Instant.now();
    }
}
