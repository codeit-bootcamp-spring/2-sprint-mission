package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID creatorId;
    private final String creatorName;
    private final UUID channelId;
    private final UUID messageId;
    public final Instant createdAt;
    public Instant updatedAt;

    private String text;

    public Message(UUID creatorId, String creatorName, UUID channelId, String text) {
        this(creatorId, creatorName, channelId, UUID.randomUUID(), Instant.now(), text);
    }

    public Message(UUID creatorId, String creatorName, UUID channelId, UUID messageId, Instant createdAt, String text) {
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.channelId = channelId;
        this.messageId = messageId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
        this.updatedAt = Instant.now();
    }
}
