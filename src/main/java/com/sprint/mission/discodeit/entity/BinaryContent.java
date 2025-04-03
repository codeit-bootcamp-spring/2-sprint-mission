package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final String fileName;
  private final String contentType;
  private final Long size;
  private final Instant createdAt;
  private final byte[] bytes;

  public BinaryContent(String filename, String contentType, Long size, byte[] bytes) {
    this.id = UUID.randomUUID();
    this.createdAt = ZonedDateTime.now().toInstant();
    this.fileName = filename;
    this.contentType = contentType;
    this.size = size;
    this.bytes = bytes;
  }
}
