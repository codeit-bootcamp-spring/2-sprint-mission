package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String content;
    private final UUID authId;
    private final UUID channelId;
    private final List<UUID> attachmentIds;

    public Message(String content, UUID authId, UUID channelId, List<UUID> attachmentIds) {
        super();
        this.content = content;
        this.authId = authId;
        this.channelId = channelId;
        this.attachmentIds = attachmentIds;
    }

    public void update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.update();
        }
    }
}