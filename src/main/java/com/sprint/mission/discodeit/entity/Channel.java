package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    private static final long SERIAL_VERSION_UID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private String name;
    private ChannelType type;
    private final List<UUID> userIds = new ArrayList<>();

    public Channel(ChannelType channelType, String name, UUID ownerId) {
        this.id = UUID.randomUUID();
        this.createdAt = ZonedDateTime.now().toInstant();
        this.updatedAt = createdAt;
        this.name = name;
        this.type = channelType;
        this.userIds.add(ownerId);
    }

    public void addMember(UUID userId) {
        userIds.add(userId);
        updateLastModified();
    }

    public void updateName(String name) {
        this.name = name;
        updateLastModified();
    }

    private void updateLastModified() {
        this.updatedAt = ZonedDateTime.now().toInstant();
    }
}
