package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "binary_contents")
@Getter
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(name = "content_type", length = 100, nullable = false)
  private String contentType;

  @Column(nullable = false)
  private Long size;

  @Column(nullable = false)
  private byte[] bytes;

  public BinaryContent() {
  }

  public BinaryContent(String fileName, String contentType, long size, byte[] bytes) {
    this.fileName = fileName;
    this.contentType = contentType;
    this.size = size;
    this.bytes = bytes;
  }
}