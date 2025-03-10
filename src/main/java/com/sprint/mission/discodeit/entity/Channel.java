package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String channelName;
    protected UUID id;
    protected Long createAt;
    protected Long updateAt;

    public Channel() {
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = createAt;
        this.channelName = "Unknown";
    }

    public UUID getId() {
        return id;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public Channel(String channelName) {
        this();
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void updateChannelName(String newChannelName) {
        this.channelName = newChannelName;
    }
}
