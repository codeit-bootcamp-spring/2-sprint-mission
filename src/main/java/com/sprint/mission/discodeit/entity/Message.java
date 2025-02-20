package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private UUID userId;  // 메시지를 작성한 사용자 ID (어떤 사용자가 작성했는지)
    private UUID channelId;  // 메세지가 속한 채널 ID (어떤 채널에서 작성되었는지)
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
    public void updateText(String newText) {
        this.text = newText;
        this.updatedAt = System.currentTimeMillis();  // 수정 시간 업데이트
    }
}
