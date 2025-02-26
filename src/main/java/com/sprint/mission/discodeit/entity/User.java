package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String userName;
    private Channel channel;

    public User(String userName,Channel channel) {
        this.userName = userName;
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.channel = channel;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", channel=" + channel.getChannelName() + // 혹은 필요한 정보를 출력
                '}';
    }
}
