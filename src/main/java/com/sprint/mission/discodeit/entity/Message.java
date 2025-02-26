package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID messageId;
    private final long createdAt;
    private long updatedAt;
    private String content;

    private User user;
    private User receiver;
    private UUID toChannelId;

    public Message(User user,String content,User receiver,UUID toChannelId) {
        if(user == null || receiver == null  || toChannelId == null) {
            throw new IllegalArgumentException("User and Receiver,channel are required");
        }
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
