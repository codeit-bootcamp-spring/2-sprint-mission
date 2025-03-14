package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public final class BinaryContent {
    private UUID id;
    private Instant createdAt;
    private UUID userId; // 사용자 프로필 이미지 저장용
    private UUID messageId; // 메시지 첨부 파일 저장용
    private byte[] content;

    public BinaryContent(UUID id, byte[] content) {
        this.id = id;
        this.content = content;
    }
}