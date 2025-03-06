package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private String content;
    private UUID channelId;
    private UUID authorId;

    public Message(UUID id, long createdAt, String content, UUID channelId, UUID authorId) {
        super(id, createdAt);
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChannelId(UUID channelId) {
        this.channelId = channelId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "Message{" +
                super.toString() +
                "content='" + content + '\'' +
                ", channelId=" + channelId +
                ", authorId=" + authorId +
                '}';
    }
}
