package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private final UUID userId;
    private final UUID channelId;
    private String content;
    private static final long serialVersionUID = 1L;

    public Message(UUID userId, UUID channelId, String content) {
        validateMessage(userId, channelId, content);
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
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

    public void update(String content) {
        if (content != null) {
            this.content = content;
            updateLastModifiedAt();
        }
    }

    protected final void updateLastModifiedAt() {
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", channelId=" + channelId +
                '}';
    }

    /*******************************
     * Validation check
     *******************************/
    private void validateMessage(UUID userId, UUID channelId, String content) {
        // 1. null check
        if (content == null) {
            throw new IllegalArgumentException("메시지 내용이 null입니다.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("채널 개설자의 ID가 없습니다.");
        }
        if (channelId == null) {
            throw new IllegalArgumentException("채널의 ID가 없습니다.");
        }
    }

}
