package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
    private String channelName;

    public Channel(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void updateChannelName(String channelName) {
        super.updateTime();
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "UUID= " + getId() +
                ", channelName='" + channelName +
                ", createdAt: " + getCreatedAt() +
                ", updatedAt: " + getUpdatedAt() +
                '}';
    }
}
