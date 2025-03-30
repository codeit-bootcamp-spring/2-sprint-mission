package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class BinaryContent extends SharedEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String fileName;
    private final byte[] binaryContent;

    public BinaryContent(String fileName, byte[] binaryContent) {
        this.fileName = fileName;
        this.binaryContent = binaryContent;
    }
}
