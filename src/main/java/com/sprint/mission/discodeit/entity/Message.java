package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {
    private String message;
    private final UUID channelId;
    private final UUID senderId;
    private final List<UUID> attachmentIds = new ArrayList<>();


    public Message(String message, UUID channelId, UUID senderId) {
        this.message = message;
        this.channelId = channelId;
        this.senderId = senderId;
    }

    public void updateMessage(String newMessage, List<UUID> attachmentIds) {
        this.message = newMessage;
        if (attachmentIds != null) {
            this.attachmentIds.clear();
            this.attachmentIds.addAll(attachmentIds);
        }
        super.update();
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, channelId, senderId);
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
                "\nchannelID: " + channelId + "\nSenderID: " + senderId + "\nMessage: " + message +
                "\nAttachments ID: " + attachmentIds +
                "\nCreatedAt: " + this.getCreatedAtFormatted() +
                "\nUpdatedAt: " + this.getUpdatedAttFormatted() +
                "\nUUID: " + this.getId();
    }
}
