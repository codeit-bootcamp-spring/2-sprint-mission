package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;

  @Column(name = "file_name", length = 255, nullable = false)
  private String filename;

  @Column(nullable = false)
  private long size;

  @Column(name = "content_type", length = 100, nullable = false)
  private String contentType;

  @Column(name = "upload_status")
  @Enumerated(EnumType.STRING)
  private BinaryContentUploadStatus uploadStatus;

  @Builder
  public BinaryContent(String filename, long size, String contentType) {
    this.filename = filename;
    this.size = size;
    this.contentType = contentType;
    this.uploadStatus = BinaryContentUploadStatus.WAITING;
  }

  public void updateStatus(BinaryContentUploadStatus status) {
    this.uploadStatus = status;
  }
}
