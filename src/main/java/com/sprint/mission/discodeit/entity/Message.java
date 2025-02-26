package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;

import java.util.UUID;

public class Message extends BaseEntity {
    private final UUID userId;
    private final UUID channelId;
    private String text;


    public Message(UUID userId, UUID channelId, String text) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.text = text;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public String getText() {
        return text;
    }

    // 업데이트 메서드 (메세지 내용)
    public void update(String text) {
        this.text = text;
        this.updatedAt = System.currentTimeMillis();  // 수정 시간 업데이트
    }

    @Override
    public String toString() {
        return "Message{" +
                "userId=" + userId +
                ", channelId=" + channelId +
                ", text='" + text + '\'' +
                ", id=" + id +
                ", createdAt=" + TimeFormatter.formatTimestamp(createdAt) +
                ", updatedAt=" + TimeFormatter.formatTimestamp(updatedAt) +
                '}';
    }
}
