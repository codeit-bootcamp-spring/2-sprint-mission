package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public final class BinaryContent {
    private UUID id;
    private Instant createdAt;
    private UUID userId; // 사용자 프로필 이미지 저장용
    private UUID messageId; // 메시지 첨부 파일 저장용
    private byte[] content;
    private String fileName;

    public BinaryContent(UUID id, byte[] content, String fileName) {
        this.id = id;
        this.content = content;
        this.fileName = fileName;
    }
}