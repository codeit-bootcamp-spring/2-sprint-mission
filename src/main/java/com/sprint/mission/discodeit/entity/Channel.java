package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class Channel {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private ChannelType type;      // PUBLIC, PRIVATE 등
    private String name;           // 채널명
    private String description;    // 채널 설명

    public Channel(ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = null;

        this.type = type;
        this.name = name;
        this.description = description;
    }

    // Getter
    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public ChannelType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // update
    public void update(ChannelType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now().getEpochSecond();
    }
}
