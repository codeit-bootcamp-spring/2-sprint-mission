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

    private UUID userId;
    private UUID channelId;
    private List<UUID> attachmentIds;

    public Message(String text, UUID userId, UUID channelId, List<UUID> attachmentIds) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.text = text;
        this.userId = userId;
        this.channelId = channelId;
        this.attachmentIds = attachmentIds;
    }

    public void updateText(String newText) {
        boolean anyValueUpdated = false;
        if (newText != null && !newText.equals(this.text)) {
            this.text = newText;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    public String toString() {
        return "[Message : " + getText() + "]";
    }
}
