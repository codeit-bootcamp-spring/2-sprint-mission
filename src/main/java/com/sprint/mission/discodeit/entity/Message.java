package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAtSeconds;
    private Instant updatedAtSeconds;
    private String content;
    private final UUID channelId;
    private final UUID authorId;
    private List<UUID> attachmentIds;

    public Message(String content, UUID channelId, UUID authorId) {
        this.id = UUID.randomUUID();
        this.createdAtSeconds = Instant.now();
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentIds = new ArrayList<>();
    }

    public void update(String newContent,List<UUID> newAttachmentIds) {
        boolean anyValueUpdated = false;

        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }

        if (newAttachmentIds != null && !newAttachmentIds.equals(this.attachmentIds)) {
            this.attachmentIds = newAttachmentIds;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updatedAtSeconds = Instant.now();
        }
    }
    //개별 파일 추가
    public void addAttachment(UUID attachmentId) {
        if (attachmentId != null && !attachmentIds.contains(attachmentId)) {
            attachmentIds.add(attachmentId);
            this.updatedAtSeconds = Instant.now();
        }
    }
    //개별 파일 삭제
    public void removeAttachment(UUID attachmentId) {
        if (attachmentId != null) {
            attachmentIds.remove(attachmentId);
            this.updatedAtSeconds = Instant.now();
        }
    }
}
