package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.util.UUID;

@Getter
public class BinaryContent extends BaseImmutableEntity {
    private final UUID ownerId;     // 파일의 소유자(User 또는 Message)
    private final String fileName;
    private final String filePath;

    public BinaryContent(UUID ownerId, String fileName, String filePath) {
        this.ownerId = ownerId;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
