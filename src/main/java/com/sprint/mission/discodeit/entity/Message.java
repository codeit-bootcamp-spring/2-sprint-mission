package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
public class Message implements Serializable, Identifiable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private List<UUID> attachmentIds;
    private Instant updatedAt;
    private String content;
    private final UUID channelId;
    private final UUID authorId;

    @Builder
    public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
        this.id = UUID.randomUUID();
        this.attachmentIds = new ArrayList<>(attachmentIds);
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }

    public void updateMessageInfo(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    public void updateMessageAttachment(List<UUID> attachmentIds) {
            this.attachmentIds = new ArrayList<>(attachmentIds);
            this.updatedAt = Instant.now();
    }

}
