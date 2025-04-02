package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private final UUID channelId;
    private final UUID userId;
    private String context;
    private List<UUID> attachmentIds;

    public Message(String context, UUID channelId, UUID userId, List<UUID> attachmentIds) {
        this.id = UUID.randomUUID();
        this.createdAt = ZonedDateTime.now().toInstant();
        this.updatedAt = createdAt;
        this.userId = userId;
        this.channelId = channelId;
        this.context = context;
        this.attachmentIds = attachmentIds;
    }

    public void updateContext(String context) {
        this.context = context;
        updateLastModified();
    }

    private void updateLastModified() {
        this.updatedAt = ZonedDateTime.now().toInstant();
    }
}
