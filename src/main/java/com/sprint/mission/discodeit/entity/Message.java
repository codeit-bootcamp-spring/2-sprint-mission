package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private String content;
    private final UUID userUUID;
    private final UUID channelUUID;

    public Message(UUID channelUUID, UUID userUUID, String content) {
        this.channelUUID = channelUUID;
        this.userUUID = userUUID;
        this.content = content;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public UUID getChannelUUID() {
        return channelUUID;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        super.updateTime();
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "UUID= " + getId() +
                ", channelUUID=" + channelUUID +
                ", userUUID=" + userUUID +
                ", content='" + content +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
