package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded = true)
@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private final UUID channelId;
    @ToString.Include
    private final UUID serverId;
    @ToString.Include
    private final UUID creatorId;
    @ToString.Include
    private String name;

    public final Instant createdAt;
    public Instant updatedAt;




    public Channel(UUID serverId, UUID creatorId, String name) {
        this(UUID.randomUUID(), serverId, creatorId,Instant.now(), name);
    }

    public Channel(UUID channelId, UUID serverId, UUID creatorId, Instant createdAt, String name) {
        this.channelId = channelId;
        this.serverId = serverId;
        this.creatorId = creatorId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
        updatedAt = Instant.now();
    }
}
