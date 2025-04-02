package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.util.UUID;

@Getter
public class BinaryContent extends BaseImmutableEntity {
    private final UUID userId;
    private final UUID messageId;
    private final String fileName;
    private final String filePath;

    public BinaryContent(UUID userId, UUID messageId, String fileName, String filePath) {
        this.userId = userId;
        this.messageId = messageId;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
