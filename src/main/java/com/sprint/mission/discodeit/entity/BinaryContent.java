package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private final Instant createdAt;
    private byte[] fileData;

    public BinaryContent(byte[] fileData) {
        this.createdAt = Instant.now();
        this.fileData = fileData;
    }

    @Override
    public String toString() {
        return "BinaryContent{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", fileData=" + Arrays.toString(fileData) +
                '}';
    }
}
