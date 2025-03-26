package com.sprint.mission.discodeit.entity;

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
    private String content;
    private List<UUID> attachmentsId;

    public Message(UUID userId, UUID channelId, String content, List<UUID> attachmentsId) {
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
        this.attachmentsId = attachmentsId;
    }

    public void updateMessage(String content, List<UUID> attachmentsId, Instant updateAt) {
        boolean anyValueUpdated = false;
        if (content != null && !content.equals(this.content)) {
            this.content = content;
            anyValueUpdated = true;
        }

        if (attachmentsId != null && !attachmentsId.equals(attachmentsId)) {
            this.attachmentsId = attachmentsId;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            super.updateUpdatedAt(Instant.now());
        }

        super.updateUpdatedAt(updateAt);
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
