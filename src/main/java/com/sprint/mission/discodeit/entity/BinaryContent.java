package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class BinaryContent {
    private UUID id;
    private UUID userId;
    private UUID messageId;
    private byte[] data;
    private Instant createdAt; // final 여부? 변할 필요 없으니깐?

    public BinaryContent(UUID id, UUID userId, UUID messageId, byte[] data) {
        this.id = id;
        this.userId = userId;
        this.messageId = messageId;
        this.data = data != null ? data : new byte[0];
        this.createdAt = Instant.now();
    }
}
