package com.sprint.mission.discodeit.entity;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

public record BinaryContent(
        UUID id,
        Instant createTime,
        UUID referenceId,
        String fileName,
        String fileType,
        Path filePath
) {
    // 주 생성자 (UUID 자동 생성)
    public BinaryContent(UUID referenceId, String fileName, String fileType, String filePath) {
        this(UUID.randomUUID(), Instant.now(), referenceId, fileName, fileType, Paths.get(filePath));
    }

    // ID를 외부에서 주입하는 보조 생성자
    public BinaryContent(UUID id, UUID referenceId, String fileName, String fileType, String filePath) {
        this(id, Instant.now(), referenceId, fileName, fileType, Paths.get(filePath));
    }
}
