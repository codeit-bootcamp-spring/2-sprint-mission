package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.ChannelType;

public class Channel extends BaseEntity {
    private ChannelType channelType;
    private String name;
    private String description;

    public Channel(ChannelType channelType, String name, String description) {
        this.channelType = channelType;
        this.name = name;
        this.description = description;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void update(String name, String description, Long updateAt) {
        this.name = name;
        this.description = description;
        super.updateUpdatedAt(updateAt);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelType=" + channelType +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
