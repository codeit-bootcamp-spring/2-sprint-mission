package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import java.io.Serial;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class BinaryContent extends BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String fileName;
  private Long size;
  private String contentType;
  private byte[] bytes;

  public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    super();
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }
}
