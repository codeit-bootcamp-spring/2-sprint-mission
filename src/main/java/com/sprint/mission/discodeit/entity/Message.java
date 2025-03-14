package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {
    private String message; // 메시지 내용
    private UUID channelId;
    private UUID senderId; // 메시지를 받는 사람


    public Message(String message, UUID channelId, UUID senderId) {
        this.message = message;
        this.channelId = channelId;
        this.senderId = senderId;
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
