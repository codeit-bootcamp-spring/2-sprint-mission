package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent {
    private final UUID id;
    private final Instant createAt;
    private User userId;
    private Message messageId;
    private byte[] data;
    private String filepath;
    private String fileName;
    private String fileType;
    private long fileSize;

    public BinaryContent(byte[] data) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.data = data;
    }
}
