package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

  @Column(nullable = false)
  private String fileName;
  @Column(nullable = false)
  private Long size;
  @Column(length = 100, nullable = false)
  private String contentType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BinaryContentUploadStatus uploadStatus;

  public BinaryContent(String fileName, Long size, String contentType, BinaryContentUploadStatus uploadStatus) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.uploadStatus = uploadStatus;
  }

  public void update(String newFileName, Long newSize, String newContentType, BinaryContentUploadStatus newUploadStatus) {
    if (fileName != null && !newFileName.equals(this.fileName)) {
      this.fileName = newFileName;
    }

    if (newSize != null && !newSize.equals(this.size)) {
      this.size = newSize;
    }

    if (newContentType != null && !newContentType.equals(this.contentType)) {
      this.contentType = newContentType;
    }

    if (newUploadStatus != null && !newUploadStatus.equals(this.uploadStatus)) {
      this.uploadStatus = newUploadStatus;
    }
  }
}
