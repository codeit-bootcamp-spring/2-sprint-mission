package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User extends BaseEntity {
    private long updatedAt;
    private String userName;
    private Channel channel;

    public User() {
        super(UUID.randomUUID(),System.currentTimeMillis());
        updatedAt = System.currentTimeMillis();
    }

    public User(String userName,Channel channel) {
        this();
        this.userName = userName;
        this.channel = channel;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.updatedAt = System.currentTimeMillis();
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
                "id=" + getId() +
                ", userName='" + userName + '\'' +
                ", channel=" + channel.getChannelName() + // 혹은 필요한 정보를 출력
                '}';
    }
}
