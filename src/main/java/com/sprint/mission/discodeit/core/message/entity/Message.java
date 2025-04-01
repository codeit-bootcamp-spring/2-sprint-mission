package com.sprint.mission.discodeit.core.message.entity;

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

    private final UUID messageId;
    private final UUID userId;
    private final UUID channelId;
    private final String userName;

    public final Instant createdAt;
    public Instant updatedAt;

    public List<UUID> attachmentIds = new ArrayList<>();
    public String text;

    public Message(UUID userId, String userName, UUID channelId, String text) {
        this(userId, userName, channelId, UUID.randomUUID(), Instant.now(), text,null);
    }

    public Message(UUID userId, String userName, UUID channelId, String text, List<UUID> attachmentIds) {
        this(userId, userName, channelId, UUID.randomUUID(), Instant.now(), text,attachmentIds);
    }

    public Message(UUID userId, String userName, UUID channelId, UUID messageId, Instant createdAt, String text, List<UUID> attachmentIds) {
        this.userId = userId;
        this.userName = userName;
        this.channelId = channelId;
        this.messageId = messageId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.text = text;
        this.attachmentIds = attachmentIds;
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
