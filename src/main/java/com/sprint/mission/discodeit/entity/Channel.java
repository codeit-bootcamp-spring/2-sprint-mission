package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private final UUID cid;
    private final Long channelCreateAt;
    private Long channelUpdateAt;
    private String channelName;
    private final User user;

    public Channel(String channelName, User user) {
        this.cid = UUID.randomUUID();
        this.channelCreateAt = System.currentTimeMillis();
        this.channelName = channelName;
        this.user = user;
    }

    public UUID getCid() {
        return cid;
    }

    public Long getChannelCreateAt() {
        return channelCreateAt;
    }

    public Long getChannelUpdateAt() {
        return channelUpdateAt;
    }

    public String getChannelName() {
        return channelName;
    }

    public User getUser() {
        return user;
    }

    public void channelUpdate(String channelName){
        this.channelUpdateAt = System.currentTimeMillis();
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "[cid: " + cid +
                ", channelCreateAt: " + channelCreateAt +
                ", channelUpdateAt: " + channelUpdateAt +
                ", channelName: " + channelName +
                ", userName: " + user.getUserName() + "]\n";
    }

}
