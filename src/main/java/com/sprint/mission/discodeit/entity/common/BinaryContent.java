package com.sprint.mission.discodeit.entity.common;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "binary_contents")
@Getter
public class BinaryContent extends BaseEntity {

  private String fileName;
  private Long size;
  private String contentType;
  private byte[] bytes;

  protected BinaryContent() {
  }

  public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    super();
    //
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }
}
