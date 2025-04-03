package com.sprint.mission.discodeit.application.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public record BinaryContentResult(UUID id, String name, String contentType, Long size,
                                  byte[] bytes) {

  public static BinaryContentResult fromEntity(BinaryContent binaryContent) {
    return new BinaryContentResult(binaryContent.getId(), binaryContent.getFileName(),
        binaryContent.getContentType(), binaryContent.getSize(), binaryContent.getBytes());
  }

  public static List<BinaryContentResult> fromEntity(List<BinaryContent> binaryContents) {
    return binaryContents.stream()
        .map(BinaryContentResult::fromEntity)
        .toList();
  }
}
