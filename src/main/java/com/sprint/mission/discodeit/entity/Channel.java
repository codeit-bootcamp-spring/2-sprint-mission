package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel extends BaseEntity {
    private long updatedAt;
    private String channelName;

    public Channel() {
        super(UUID.randomUUID(),System.currentTimeMillis());
        updatedAt = System.currentTimeMillis();
    }
    public Channel(String channelName) {
        this();
        this.channelName = channelName;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
