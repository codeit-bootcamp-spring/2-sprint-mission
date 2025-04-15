package com.sprint.mission.discodeit.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class BinaryContent extends BaseUpdatableEntity {

  @Column(nullable = false, length = 255)
  private String fileName;

  @Column(nullable = false)
  private Long size;

  @Column(nullable = false, length = 100)
  private String contentType;

  @Lob
  @Column(nullable = false)
  private byte[] bytes;

  public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }
}
