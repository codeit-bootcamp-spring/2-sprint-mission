package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;

import java.util.UUID;

public class Message extends BaseEntity{
    private final UUID channelId;
    private final UUID userId;
    private String text;

    public Message(UUID channelId, UUID UserId, String text) {
        super();
        this.channelId = channelId;
        this.userId = UserId;
        this.text = text;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setUpdatedAt();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", userId=" + userId +
                ", text='" + text + '\'' +
                ", createdAt=" + TimeFormatter.format(createdAt, "yyyy-MM-dd HH:mm:ss") +
                ", updatedAt=" + TimeFormatter.format(updatedAt, "yyyy-MM-dd HH:mm:ss") +
                '}';
    }
}