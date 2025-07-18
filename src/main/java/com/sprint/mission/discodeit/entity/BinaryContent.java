package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "binary_contents")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

  @Column(nullable = false)
  private String fileName;
  @Column(nullable = false)
  private Long size;
  @Column(length = 100, nullable = false)
  private String contentType;
  @Column(nullable = false)
  private BinaryContentUploadStatus uploadStatus;

  public BinaryContent(String fileName, Long size, String contentType) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.uploadStatus = BinaryContentUploadStatus.WAITING;
  }
}
