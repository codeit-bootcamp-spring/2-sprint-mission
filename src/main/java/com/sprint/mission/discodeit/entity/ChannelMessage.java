package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class ChannelMessage extends Message {
    UUID channelId;

    public ChannelMessage(UUID senderId, String content, UUID channelId) {
        super(senderId, content);
        this.channelId = channelId;
    }

    public UUID getChannelId() {
        return channelId;
    }
}
