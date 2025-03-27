package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BinaryData {
    private String binaryFileName;
    private byte[] data;

    public BinaryData(String fileName, byte[] data) {
        String extension = extractExtension(fileName);
        this.binaryFileName = UUID.randomUUID().toString() + "." + extension;
        this.data = data;
    }

    private String extractExtension(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
    }
}
