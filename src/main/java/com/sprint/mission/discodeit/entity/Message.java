package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;


@Getter
public class Message extends MainDomain {
    private String message;
    private final UUID userId;
    private final UUID channelId;
    private List<UUID> attachmentIds;

    public Message(String message, UUID userId, UUID channelId) {
        super();
        this.message = message;
        this.userId = userId;
        this.channelId = channelId;
    }

    public Message(String message, UUID userId, UUID channelId, List<UUID> attachmentIds) {
        this(message, userId, channelId);
        this.attachmentIds = attachmentIds;
    }


    public void updateMessage(String newMessage) {
        boolean anyValueUpdated = false;
        if (newMessage != null && !newMessage.equals(message)) {
            message = newMessage;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            update();
        }
    }

    @Override
    public String toString() {
        return "Message{" + "\n" +
                "messageID=" + getId() + "\n" +
                ", message=" + message + "\n" +
                ", userId=" + userId +
                ", channelId=" + channelId +
                '}';

    }

}
