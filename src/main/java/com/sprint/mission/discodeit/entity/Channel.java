package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel extends BaseEntity {
    private String channelName;
    private List<UUID> channelUser;
    private List<UUID> channelMessage;

    public Channel(String channelName) {
        super();
        this.channelName = channelName;
        this.channelUser = new ArrayList<>();
        this.channelMessage = new ArrayList<>();
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
        update();
    }

    public List<UUID> getChannelUser() {
        return channelUser;
    }

    public int getChannelUserCount() {
        return channelUser.size();
    }

    public List<UUID> getChannelMessage() {
        return channelMessage;
    }
}