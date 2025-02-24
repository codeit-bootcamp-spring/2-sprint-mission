package com.sprint.mission.discodeit.entity;

public class Channel extends CommonEntity {
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
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                '}';
    }
}
