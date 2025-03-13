package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message extends CommonEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private UUID channelId;

    public Message(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "[Message " +super.toString();
    }
}
