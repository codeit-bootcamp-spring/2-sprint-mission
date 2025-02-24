package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID messageId; //메세지 id
    private final long createdAt;//메세지 생성 시간
    private long updatedAt;//메세지 수정 업데이트 시간
    private String content;//메세지 내용

    private User user;//발신인
    private User receiver;//수신인
    private UUID toChannelId;//채널 id

    public Message(User user,String content,User receiver,UUID toChannelId) {
        this.user = user;
        this.content=content;
        this.receiver=receiver;
        this.toChannelId=toChannelId;

        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.messageId=UUID.randomUUID();
    }

    public UUID getMessageId() {
        return messageId;
    }

    public long getCreatedAt() {
        return createdAt;
    }


    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public UUID getToChannelId() {
        return toChannelId;
    }

    public void setToChannelId(UUID toChannelId) {
        this.toChannelId = toChannelId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", receiver=" + receiver +
                ", toChannelId=" + toChannelId +
                '}';
    }
}
