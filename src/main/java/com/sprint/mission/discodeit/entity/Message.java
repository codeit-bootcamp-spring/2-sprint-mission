package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Message extends BaseEntity{
    private String messageContent;
    private UUID channelId;
    private UUID userId;

    public Message(UUID channelId, UUID userId, String messageContent) {
        super();
        this.channelId = channelId;
        this.userId = userId;
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "{" +
                "messageContent='" + messageContent + '\'' +
                ", channelId=" + channelId +
                ", userId=" + userId +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
