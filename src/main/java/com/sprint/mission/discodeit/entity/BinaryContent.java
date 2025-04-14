package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BinaryContent extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private String fileName;
  private Long size;
  private String contentType;
  private byte[] bytes;
}
