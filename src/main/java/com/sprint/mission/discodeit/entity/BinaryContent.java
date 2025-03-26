package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BinaryContent extends SharedEntity{
    private final String fileName;
    private final byte[] binaryContent;

    public BinaryContent(String fileName, byte[] binaryContent) {
        this.fileName = fileName;
        this.binaryContent = binaryContent;
    }
}
