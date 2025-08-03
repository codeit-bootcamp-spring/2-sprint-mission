package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChannelEvent {
    private final ChannelEventType eventType;
    private final Channel channel;
    private final UUID channelId;

    public ChannelEvent(ChannelEventType eventType, Channel channel) {
        this.eventType = eventType;
        this.channel = channel;
        this.channelId = channel.getId();
    }

    public ChannelEvent(ChannelEventType eventType, UUID channelId) {
        this.eventType = eventType;
        this.channel = null;
        this.channelId = channelId;
    }

    public enum ChannelEventType {
        CREATED,
        UPDATED,
        DELETED
    }
} 