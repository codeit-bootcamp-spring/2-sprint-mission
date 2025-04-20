package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "binary_contents")
@Getter
@Setter
@NoArgsConstructor
public class BinaryContent extends BaseEntity {

  @Column(nullable = false, length = 255)
  private String fileName;

  @Column(nullable = false)
  private Long size;

  @Column(name = "content_type", nullable = false, length = 100)
  private String contentType;

  public BinaryContent(String fileName, Long size, String contentType) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }
}
