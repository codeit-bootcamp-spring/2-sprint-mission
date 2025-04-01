package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private final UUID userId;
    private final UUID channelId;
    private String content;
    private List<UUID> attachmentIds;

    public Message(UUID userId, UUID channelId, String content, List<UUID> attachmentIds) {
        validateMessage(userId, channelId, content);
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
        this.attachmentIds = attachmentIds;
    }

    public void update(String content, List<UUID> attachmentIds) {
        if (content != null) {
            this.content = content;
            updateLastModifiedAt();
        }
        if (attachmentIds != null){
            this.attachmentIds = attachmentIds;
            updateLastModifiedAt();
        }
    }

    private void updateLastModifiedAt() {
        this.updatedAt = Instant.now();
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
