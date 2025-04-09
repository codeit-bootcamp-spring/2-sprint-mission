package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Message implements Serializable, Comparable<Message> {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private String content;

    private UUID authorId;
    private UUID channelId;

    private List<UUID> attachedImageIds;

    public Message(String content, UUID authorId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;

        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
        this.attachedImageIds = new ArrayList<>();
    }

    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = Instant.now();
    }

    public void updateImages(UUID imageId) {
        this.attachedImageIds.add(imageId);
    }

    @Override
    public int compareTo(Message message) {
        return this.createdAt.compareTo(message.createdAt);
    }
}
