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
    private final List<UUID> attachmentIds;


    public Message(String message, UUID channelId, UUID senderId) {
        this.message = message;
        this.channelId = channelId;
        this.senderId = senderId;
        this.attachmentIds = new ArrayList<>();
    }

    public void updateMessage(String newMessage) {
        this.message = newMessage;
        super.update();
        System.out.printf("내용이 [ %s ] 로 변경되었습니다.", this.message);
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
        return "\nchannelID: " + channelId + "\nSenderID: " + senderId + "\nMessage: " + message +
                "\nCreatedAt: " + this.getCreatedAtFormatted() +
                "\nUpdatedAt: " + this.getUpdatedAttFormatted() +
                "\nUUID: " + this.getId();
    }
}
