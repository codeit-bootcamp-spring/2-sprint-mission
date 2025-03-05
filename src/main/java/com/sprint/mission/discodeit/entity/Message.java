package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private UUID channelId;
    private String content;
    protected UUID id;
    protected Long createAt;
    protected Long updateAt;

    public Message(UUID userId, UUID channelId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
        this.createAt = System.currentTimeMillis();
        this.updateAt = createAt;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String newContent) {
        if (newContent == null || newContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        this.content = newContent;
        this.updateAt = System.currentTimeMillis();
    }
}