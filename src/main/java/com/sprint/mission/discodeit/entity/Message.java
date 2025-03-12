package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;

import java.io.Serializable;
import java.util.UUID;

public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID userId;
    private final UUID channelId;
    private String text;

    public Message(UUID userId, UUID channelId, String text) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.text = text;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public String getText() {
        return text;
    }

    public void update(String text, long updatedAt) {
        this.text = text;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userId=" + userId +
                ", channelId=" + channelId +
                ", text='" + text + '\'' +
                ", id=" + id +
                ", createdAt=" + TimeFormatter.formatTimestamp(createdAt) +
                ", updatedAt=" + TimeFormatter.formatTimestamp(updatedAt) +
                '}';
    }
}
