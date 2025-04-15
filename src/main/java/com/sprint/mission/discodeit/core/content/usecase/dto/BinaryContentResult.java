package com.sprint.mission.discodeit.core.content.usecase.dto;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import java.util.UUID;
import lombok.Builder;

@Builder
public record BinaryContentResult(
    UUID id,
    String fileName,
    Long size,
    String contentType
) {

  public static BinaryContentResult create(BinaryContent binaryContent) {
    return BinaryContentResult.builder()
        .id(binaryContent.getId())
        .fileName(binaryContent.getFileName())
        .size(binaryContent.getSize())
        .contentType(binaryContent.getContentType()).build();
  }
}
