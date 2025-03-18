package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent {
    private final UUID id;
    private final Instant createdAtSeconds;
    private byte[] data;
    private final UUID userId; //profileId와 연결
    private final UUID messageId; //attachments와 연결

    public BinaryContent(byte[] data, UUID userId, UUID messageId) {
        if ((userId != null && messageId != null) || (userId == null && messageId == null)) {
            throw new IllegalArgumentException("BinaryContent는 userId 또는 messageId 중 하나만 설정해야 합니다.");
        }

        this.id = UUID.randomUUID();
        this.createdAtSeconds = Instant.now();
        this.data = data;
        this.userId = userId;
        this.messageId = messageId;
    }
}
