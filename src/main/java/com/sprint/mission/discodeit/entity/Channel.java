package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private final UUID channelId;
    private final long createdAt;
    private long updatedAt;
    private String channelName;

    public Channel(String channelName) {
        this.channelId = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;

        this.channelName = channelName;
    }

    public UUID getId() {
        return channelId;
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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
