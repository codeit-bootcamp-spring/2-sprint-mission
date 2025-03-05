package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private final UUID userId;
    private final UUID channelId;
    private String content;

    public Message(UUID userId, UUID channelId, String content) {
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
    }

    public UUID getUserId() { return userId; }
    public UUID getChannelId() { return channelId; }
    public String getContent() { return content; }

    public void setContent(String content) {
        this.content = content;
        super.updateUpdateAt();
    }

    @Override
    public String toString() {
        return "Message{" +
                "userId=" + userId +
                ", channelId=" + channelId +
                ", content='" + content + '\'' +
                "} " + super.toString();
    }
}