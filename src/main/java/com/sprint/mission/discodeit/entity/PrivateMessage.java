package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class PrivateMessage extends Message {
    private final UUID receiverId;

    public PrivateMessage(UUID senderId, String content, UUID receiverId) {
        super(senderId, content);
        this.receiverId = receiverId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    @Override
    public String toString() {
        return "PrivateMessage{" +
                "receiverId=" + receiverId +
                super.toString() +
                '}';
    }
}
