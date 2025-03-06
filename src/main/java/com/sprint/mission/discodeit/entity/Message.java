package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private long updatedAt;
    private String content;

    private User user;
    private User receiver;
    private UUID toChannelId;

    private static final boolean SOME_VALUE_IS_NULL = Boolean.TRUE;
    private static final boolean ALL_PARAMS_HAS_VALUE = Boolean.FALSE;

    public Message() {
        super(UUID.randomUUID(),System.currentTimeMillis());
        this.updatedAt = System.currentTimeMillis();
    }

    public Message(User user,String content,User receiver,UUID toChannelId) {
        this();
        if(checkRequiredField(user,receiver,toChannelId)==SOME_VALUE_IS_NULL) {
            throw new IllegalArgumentException("User and Receiver,channel are required");
        }
        this.user = user;
        this.content=content;
        this.receiver=receiver;
        this.toChannelId=toChannelId;

        this.updatedAt = System.currentTimeMillis();
    }

    private boolean checkRequiredField(User user,User receiver,UUID toChannelId) {
        return (user==null || receiver==null || toChannelId==null)? SOME_VALUE_IS_NULL :ALL_PARAMS_HAS_VALUE;
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
                "messageId=" + getId() +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + updatedAt +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", receiver=" + receiver +
                ", toChannelId=" + toChannelId +
                '}';
    }
}
