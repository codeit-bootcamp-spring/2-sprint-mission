package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class BinaryContent extends BaseEntity implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;

  private String filename;
  private long size;
  private String contentType;
  private byte[] bytes;

  @Builder
  public BinaryContent(String filename, long size, String contentType, byte[] bytes) {
    this.filename = filename;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }
}
