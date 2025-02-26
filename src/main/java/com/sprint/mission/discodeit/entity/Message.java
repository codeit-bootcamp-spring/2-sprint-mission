package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private UUID senderId;  // User의 UUID 저장
    private UUID channelId; // Channel의 UUID 저장
    private String content;

    public Message(UUID senderId, UUID channelId, String content) {
        super();
        this.senderId = senderId;
        this.channelId = channelId;
        this.content = content;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void updateSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public void updateChannelId(UUID channelId) {
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderId= " + senderId +
                ", sendTime= " + getCreatedAt() +
                ", channelId= " + channelId +
                ", content= '" + content + '\'' +
                '}';
    }
}
