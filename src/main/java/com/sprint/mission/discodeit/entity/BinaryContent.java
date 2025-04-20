package com.sprint.mission.discodeit.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name="binary_contents")
public class BinaryContent extends BaseEntity {

  @Column(name="file_name", nullable = false, length = 255)
  private String fileName;

  @Column(name="size", nullable = false)
  private Long size;

  @Column(name="content_type", nullable = false, length = 100)
  private String contentType;


  public BinaryContent(String fileName, Long size, String contentType) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }
}
