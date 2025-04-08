package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {
    private String content;
    private final UUID channelId;
    private final UUID authorId;
    private final List<UUID> attachmentIds;


    public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentIds = attachmentIds;
    }

    public void updateMessage(String newMessage) {
        this.content = newMessage;
        super.update();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Message message) {
            return message.getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "\nMessage ID: " + this.getId() +
                "\nchannelID: " + channelId + "\nSenderID: " + authorId + "\nMessage: " + content +
                "\nAttachments ID: " + attachmentIds +
                "\nCreatedAt: " + this.getCreatedAt() +
                "\nUpdatedAt: " + this.getUpdatedAt() +
                "\nUUID: " + this.getId();
    }
}
