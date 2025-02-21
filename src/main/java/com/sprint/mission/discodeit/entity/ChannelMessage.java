package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class ChannelMessage extends Message {
    UUID channelId;

    public ChannelMessage(UUID sender, String content, UUID channelId) {
        super(sender, content);
        this.channelId = channelId;
    }

    public UUID getChannelId() {
        return channelId;
    }
}
