package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.async.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "binary_contents")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(nullable = false)
  private Long size;

  @Column(name = "content_type", nullable = false)
  private String contentType;

  @Column(name = "upload_status")
  @Enumerated(EnumType.STRING)
  private BinaryContentUploadStatus uploadStatus;

  public BinaryContent(
      String fileName,
      Long size,
      String contentType
  ) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.uploadStatus = BinaryContentUploadStatus.WAITING;
  }

  public void updateStatus(
      BinaryContentUploadStatus uploadStatus
  ) {
    this.uploadStatus = uploadStatus;
  }
}
