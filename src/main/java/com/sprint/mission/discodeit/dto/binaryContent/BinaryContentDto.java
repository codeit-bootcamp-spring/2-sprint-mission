package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    String filename,
    Long size,
    String contentType,
    byte[] bytes
) {

  public static BinaryContentDto from(BinaryContent entity) {
    if (entity == null) {
      return null;
    }

    return new BinaryContentDto(
        entity.getId(),
        entity.getFileName(),
        entity.getSize(),
        entity.getContentType(),
        entity.getBytes()
    );
  }
}
