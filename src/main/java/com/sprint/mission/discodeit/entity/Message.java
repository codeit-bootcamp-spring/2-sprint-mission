package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private  UUID messageId;
    private final UUID creatorId;
    private final String creatorName;
    private final UUID channelId;

    public final Instant createdAt;
    public Instant updatedAt;

    public List<UUID> attachmentIds = new ArrayList<>();
    public String text;

    public Message(UUID creatorId, String creatorName, UUID channelId, String text) {
        this(creatorId, creatorName, channelId, UUID.randomUUID(), Instant.now(), text);
    }

    public Message(UUID creatorId, String creatorName, UUID channelId, UUID messageId, Instant createdAt, String text) {
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.channelId = channelId;
        this.messageId = messageId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.text = text;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
        this.updatedAt = Instant.now();
    }

    public void setText(String text) {
        this.text = text;
        this.updatedAt = Instant.now();
    }

    public void setAttachmentIds(List<UUID> attachmentIds) {
        this.attachmentIds = attachmentIds;
        this.updatedAt = Instant.now();
    }
}
