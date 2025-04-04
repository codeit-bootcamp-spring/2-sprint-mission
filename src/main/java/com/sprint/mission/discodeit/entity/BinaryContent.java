package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;
  @Builder.Default
  private UUID id = UUID.randomUUID();
  private String fileName;
  private Long size;
  private String contentType;
  private byte[] bytes;
  @Builder.Default
  private final Instant createdAt = Instant.now();
}
