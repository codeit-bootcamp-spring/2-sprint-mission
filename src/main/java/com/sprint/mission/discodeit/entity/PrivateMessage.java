package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class PrivateMessage extends Message {
    private UUID receiverId;

    public PrivateMessage(UUID senderId, String content, UUID receiverId) {
        super(senderId, content);
        this.receiverId = receiverId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }
}
