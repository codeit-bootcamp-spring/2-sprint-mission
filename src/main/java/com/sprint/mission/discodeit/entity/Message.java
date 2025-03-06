package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID authorId;
    private final UUID channelId;
    private String content;

    public Message(UUID authorId, UUID channelId, String content) {
        super();
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            updateUpdatedAt();
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id + '\'' +
                ", authorId=" + authorId + '\'' +
                ", channelId=" + channelId + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
