package com.sprint.mission.discodeit.core.content.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class BinaryContent implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final UUID id;
  public final Instant uploadAt;

  private String fileName;
  private long size;
  private String contentType;
  private byte[] bytes;

  private BinaryContent(String fileName, long size, String contentType, byte[] bytes) {
    this.id = UUID.randomUUID();
    this.uploadAt = Instant.now();

    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }

  public static BinaryContent create(String fileName, long size, String contentType,
      byte[] bytes) {
    return new BinaryContent(fileName, size, contentType, bytes);
  }

  private static class Validator {

    public void validate(String fileName) {
      validateFileName(fileName);
    }

    public void validateFileName(String fileName) {

    }
  }
}
