package com.sprint.mission.discodeit.adapter.inbound.content.response;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Binary Content Response")
public record BinaryContentResponse(
    @Schema(description = "Binary Content Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,
    @Schema(description = "Binary Content 생성 시각", example = "2025-04-03T01:49:44.983Z")
    Instant createdAt,
    @Schema(description = "Binary Content File name", example = "string")
    String fileName,
    @Schema(description = "Binary Content size", example = "9007199254740991")
    Long size,
    @Schema(description = "Binary Content contentType", example = "string")
    String contentType,
    @Schema(description = "Binary Content bytes", example = "string")
    String bytes
) {

  public static BinaryContentResponse create(BinaryContent content) {
    return BinaryContentResponse.builder()
        .id(content.getId())
        .createdAt(content.getUploadAt())
        .fileName(content.getFileName())
        .size(content.getSize())
        .contentType(content.getContentType())
        .bytes(Base64.getEncoder().encodeToString(content.getBytes()))
//        .bytes(content.getBytes())
        .build();
  }

}
