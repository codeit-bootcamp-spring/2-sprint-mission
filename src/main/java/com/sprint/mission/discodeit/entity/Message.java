package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;


@Getter
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID senderId;  // User의 UUID 저장
    private final UUID channelId; // Channel의 UUID 저장
    private String content;

    public Message(UUID senderId, UUID channelId, String content) {
        super();
        this.senderId = senderId;
        this.channelId = channelId;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
        updateTimestamp();
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderId= " + senderId + '\'' +
                ", sendTime= " + getCreatedAt() + '\'' +
                ", channelId= " + channelId + '\'' +
                ", content= '" + content + '\'' +
                ", lastUpdateTime= " + getUpdatedAt() +
                '}';
    }
}
