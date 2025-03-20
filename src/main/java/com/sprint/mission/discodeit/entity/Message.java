package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;
import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID userId;
    private final UUID channelId;
    private String text;
    private List<UUID> attachmentIds;

    public Message(UUID userId, UUID channelId, String text, List<UUID> attachmentIds) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.text = text;
        this.attachmentIds = attachmentIds;
    }

    public void update(String text, Instant updatedAt) {
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
