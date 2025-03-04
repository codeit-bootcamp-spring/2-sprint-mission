package com.sprint.mission.discodeit.entity;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Message {
    private final UUID id;
    private final Long createdAt;
    private final Long updatedAt;
    private final String content;

    private final UUID senderId;
    private final UUID channelId;

    public Message(String content, UUID senderId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;

        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", content='" + content + '\'' +
                ", sender='" + senderId + '\'' +
                '}';
    }
}
