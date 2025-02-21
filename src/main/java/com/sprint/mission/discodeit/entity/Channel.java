package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends Common {
    private String channelName;
    private String description;
    private List<Message> messages;

    public Channel(String channelName, String description)  {
        this.channelName = channelName;
        this.description = description;
        this.messages = new ArrayList<Message>();
    }

    public String getChannelName() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void updateChannel(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
        super.update();
    }
}
