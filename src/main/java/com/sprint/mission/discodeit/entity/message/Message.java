package com.sprint.mission.discodeit.entity.message;

import com.sprint.mission.discodeit.entity.base.BaseEntity;

import java.util.UUID;

public class Message extends BaseEntity {
    private final UUID senderId;
    private String content;
    private final UUID channelId;

    public Message(UUID senderId, String content, UUID channelId) {
        super();
        this.senderId = senderId;
        this.channelId = channelId;
        setContent(content);
    }

    private void setContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 메세지입니다.");
        }
        this.content = content;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void update(String content) {
        setContent(content);
        updateModifiedAt();
    }

    public UUID getChannelId() {
        return channelId;
    }

    @Override
    public String toString() {
        return "sender=" + senderId +
                ", content='" + content + '\'' +
                super.toString();
    }
}

