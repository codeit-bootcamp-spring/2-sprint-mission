package com.sprint.mission.discodeit.domain.storage.dto;

import com.sprint.mission.discodeit.domain.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.storage.entity.BinaryContentUploadStatus;
import java.util.UUID;
import lombok.Builder;

@Builder
public record BinaryContentDto(
    UUID id,
    String fileName,
    Long size,
    String contentType,
    BinaryContentUploadStatus uploadStatus
) {

  public static BinaryContentDto from(BinaryContent binaryContent) {
    return BinaryContentDto.builder()
        .id(binaryContent.getId())
        .fileName(binaryContent.getFileName())
        .size(binaryContent.getSize())
        .contentType(binaryContent.getContentType())
        .uploadStatus(binaryContent.getUploadStatus())
        .build();
  }

}
