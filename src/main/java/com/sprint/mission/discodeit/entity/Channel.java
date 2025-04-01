package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String channelName;
    private String description;
    private ChannelType type;

    public Channel(ChannelType type, String channelName, String description) {
        super();
        this.type = type;
        this.channelName = channelName;
        this.description = description;
    }

    public void updateChannelType(ChannelType type) {
        this.type = type;
        updateTimestamp();
    }

    public void updateDescription(String description) {
        this.description = description;
        updateTimestamp();
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        updateTimestamp();
    }


    @Override
    public String toString() {
        return "Channel{" +
                "channelId='" + getId() + '\'' +
                "channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                ", channelType=" + type + '\'' +
                ", lastUpdateTime= " + getUpdatedAt() +
                '}';
    }
}
