package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;

public class Channel extends BaseEntity implements Serializable {
    private String channelName;

    @Serial
    private static final long serialVersionUID = 1L;

    public Channel(String channelName) {
        super();
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "< 채널 > " + channelName + ", " + getId();
    }
}
