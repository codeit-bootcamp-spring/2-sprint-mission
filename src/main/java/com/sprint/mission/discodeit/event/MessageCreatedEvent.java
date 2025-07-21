package com.sprint.mission.discodeit.event;

import lombok.Getter;

import java.util.UUID;

@Getter
public class MessageCreatedEvent {
    private final UUID channelId;
    private final String message;

    public MessageCreatedEvent(UUID channelId, String message) {
        this.channelId = channelId;
        this.message = message;
    }
}