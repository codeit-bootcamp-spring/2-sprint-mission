package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@EntityScan
@Getter
public class ReadStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 104L;

    private final UUID id;
    private final UUID channelId; // 채널
    private final UUID userId; // 유저
    private UUID lastReadMessageId; // 마지막으로 메시지
    private ZonedDateTime lastReadAt; // 마지막으로 메시지를 읽은 시간
    private final ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public ReadStatus(UUID channelId, UUID userId, UUID lastReadMessageId) {
        this.id = UUID.randomUUID();
        this.channelId = channelId;
        this.userId = userId;
        this.lastReadMessageId = lastReadMessageId;
        this.lastReadAt = ZonedDateTime.now();
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now(); //
    }

    public void updateLastReadMessage(UUID messageId) {
        this.lastReadMessageId = messageId;
        this.lastReadAt = ZonedDateTime.now(); // 메시지 읽은 시간 업데이트
        setUpdatedAt();
    }

    private void setUpdatedAt() {
        this.updatedAt = ZonedDateTime.now();
    }
}