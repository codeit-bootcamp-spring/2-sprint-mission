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
  private String contentType;
  private String extension;
  private Long size;
  private byte[] bytes;

  private BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    this.id = UUID.randomUUID();
    this.uploadAt = Instant.now();

    this.fileName = fileName;
    this.contentType = contentType;
    this.extension = extractExtension(fileName);

    this.size = size;
    this.bytes = bytes;
  }

  public static BinaryContent create(String fileName, Long size, String contentType,
      byte[] bytes) {
    return new BinaryContent(fileName, size, contentType, bytes);
  }

  private static String extractExtension(String fileName) {
    String[] nameSplit = fileName.split("\\.");
    if (nameSplit.length > 1) {
      return "." + nameSplit[1].toLowerCase();
    } else {
      return "";
    }
  }

  private static class Validator {

    public void validate(String fileName) {
      validateFileName(fileName);
    }

    public void validateFileName(String fileName) {

    }
  }
}
