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
  private final String fileName;
  private final Long size;
  private final String contentType;
  private final byte[] bytes;

  public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    validateBinaryContent(fileName, size, contentType, bytes);
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }

  @Override
  public String toString() {
    return "BinaryContent{" +
        "id=" + id +
        ", createdAt=" + createdAt +
        ", fileName='" + fileName + '\'' +
        ", size=" + size +
        ", contentType='" + contentType + '\'' +
        ", bytes=" + Arrays.toString(bytes) +
        '}';
  }

  /*******************************
   * Validation check
   *******************************/
  private void validateBinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    // 1. null check
    if (fileName == null) {
      throw new IllegalArgumentException("fileName이 없습니다.");
    }
    if (size == null) {
      throw new IllegalArgumentException("size가 없습니다.");
    }
    if (contentType == null) {
      throw new IllegalArgumentException("contentType이 없습니다.");
    }
    if (bytes == null) {
      throw new IllegalArgumentException("파일 데이터가 없습니다.");
    }

    // 2. 파일 데이터 크기 check
    if (size <= 0) {
      throw new IllegalArgumentException("파일 크기가 유효하지 않습니다.");
    }
  }

}
