package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(nullable = false)
  private Long size;

  @Column(name = "content_type", nullable = false, length = 100)
  private String contentType;

  public BinaryContent(String fileName, Long size, String contentType) {
    super();
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }

  protected BinaryContent() {
  }
}
