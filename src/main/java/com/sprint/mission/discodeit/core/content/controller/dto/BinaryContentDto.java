package com.sprint.mission.discodeit.core.content.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

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

}
