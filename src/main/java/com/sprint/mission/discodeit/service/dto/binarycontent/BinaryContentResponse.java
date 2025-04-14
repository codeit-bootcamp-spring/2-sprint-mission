package com.sprint.mission.discodeit.service.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;
import lombok.Builder;

@Builder
public record BinaryContentResponse(
    UUID id,
    String fileName,
    Long size,
    String contentType
) {

  public static BinaryContentResponse of(BinaryContent binaryContent) {
    return BinaryContentResponse.builder()
        .id(binaryContent.getId())
        .fileName(binaryContent.getFileName())
        .size(binaryContent.getSize())
        .contentType(binaryContent.getContentType())
        .build();
  }
}
