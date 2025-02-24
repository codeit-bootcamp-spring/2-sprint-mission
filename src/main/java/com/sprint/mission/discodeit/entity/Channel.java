package com.sprint.mission.discodeit.entity;

public class Channel extends MainDomain {
    private String channelName;

    public Channel(String channelName) {
        super();
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void updateChannel(String channelName) {
        this.channelName = channelName;
        update();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                '}';
    }
}
