package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse(
    UUID id,
    Instant createdAt,
    String fileName,
    Long size,
    String contentType,
    byte[] bytes
) {

  public static BinaryContentResponse of(BinaryContent binaryContent) {
    return new BinaryContentResponse(
        binaryContent.getId(), binaryContent.getCreatedAt(),
        binaryContent.getFileName(), binaryContent.getSize(),
        binaryContent.getContentType(), binaryContent.getBytes()
    );
  }
}
