package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private String text;
    private List<UUID> attachmentIds;

    private UUID userId;
    private UUID channelId;

    public Message(String text, UUID userId, UUID channelId, List<UUID> attachmentIds) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;

        this.text = text;
        this.userId = userId;
        this.channelId = channelId;
        this.attachmentIds = attachmentIds;
    }

    public void updateText(String text) {
        this.text = text;
        updatedAt = Instant.now();
    }

    public String toString() {
        return "[Message : " + getText() + "]";
    }
}
