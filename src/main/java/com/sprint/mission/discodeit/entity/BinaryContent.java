package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private final byte[] fileData;
    private final String filePath;
    private final String fileName;
    private final String fileType;
    private final long fileSize;

    public BinaryContent(byte[] fileData, String filePath, String fileName, String fileType, long fileSize) {
        validateBinaryContent(fileData, filePath, fileName, fileType, fileSize);
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileData = fileData;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "BinaryContent{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", fileData=" + Arrays.toString(fileData) +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }

    /*******************************
     * Validation check
     *******************************/
    private void validateBinaryContent(byte[] fileData, String filePath, String fileName, String fileType, long fileSize){
        // 1. null check
        if (fileData == null) {
            throw new IllegalArgumentException("fileData가 없습니다.");
        }
        if (filePath == null) {
            throw new IllegalArgumentException("filePath가 없습니다.");
        }
        if (fileName == null) {
            throw new IllegalArgumentException("fileName이 없습니다.");
        }
        if (fileType == null) {
            throw new IllegalArgumentException("fileType이 없습니다.");
        }
        // 2. 파일 데이터 크기 체크
        if (fileData.length == 0) {
            throw new IllegalArgumentException("파일 데이터가 비어 있습니다.");
        }
        if (fileSize <= 0) {
            throw new IllegalArgumentException("파일 크기가 유효하지 않습니다.");
        }
    }

}
