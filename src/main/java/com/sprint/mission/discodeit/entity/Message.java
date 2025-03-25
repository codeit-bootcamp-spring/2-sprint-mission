package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {
    private String content;
    private UUID channelId;
    private UUID authorId;
    private List<UUID> attachmentIds = new ArrayList<>();

    public Message(UUID id, Instant createdAt, String content, UUID channelId, UUID authorId, UUID attachmentId) {
        super(id, createdAt);
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentIds.add(attachmentId);
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
