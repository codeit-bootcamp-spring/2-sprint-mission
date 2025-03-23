package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID userId;
    private final UUID channelId;
    private String content;
    protected final UUID id;
    protected final Instant createAt;
    protected Instant updateAt;

    public Message(UUID userId, UUID channelId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
        this.createAt = Instant.now();
        this.updateAt = createAt;
    }

    public void updateContent(String newContent) {
        if (newContent == null || newContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        this.content = newContent;
        this.updateAt = Instant.now();
    }
}