package com.sprint.mission.discodeit.core.content.controller.dto;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Binary Content Response")
public record BinaryContentDto(
    @Schema(description = "Binary Content Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,
    @Schema(description = "Binary Content File username", example = "string")
    String fileName,
    @Schema(description = "Binary Content size", example = "9007199254740991")
    Long size,
    @Schema(description = "Binary Content contentType", example = "string")
    String contentType
) {

  public static BinaryContentDto create(BinaryContent binaryContent) {
    return BinaryContentDto.builder()
        .id(binaryContent.getId())
        .fileName(binaryContent.getFileName())
        .size(binaryContent.getSize())
        .contentType(binaryContent.getContentType()).build();
  }
}
