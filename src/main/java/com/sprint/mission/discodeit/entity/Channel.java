package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ChannelType type;
    private String channelName;
    private String description;

    public Channel(ChannelType type, String channelName, String description) {
        super();
        this.type = type;
        this.channelName = channelName;
        this.description = description;
    }

    public ChannelType getType() {
        return type;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }

    public void update(String newChannelName, String newDescription) {
        boolean anyValueUpdated = false;
        if (newChannelName != null && !newChannelName.equals(this.channelName)) {
            this.channelName = newChannelName;
            anyValueUpdated = true;
        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.update();
        }
    }
}
