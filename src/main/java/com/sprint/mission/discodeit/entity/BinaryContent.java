package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class BinaryContent extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String fileName;
    private final byte[] fileData;
    private final Long fileSize;
    private final String contentType;

    public BinaryContent(String fileName, byte[] fileData, Long fileSize, String contentType) {
        super();
        this.fileName = fileName;
        this.fileData = fileData;
        this.fileSize = fileSize;
        this.contentType = contentType;
    }
}

