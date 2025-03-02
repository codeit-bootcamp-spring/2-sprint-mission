package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
    private String channelName;

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
