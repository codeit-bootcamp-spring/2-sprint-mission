package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BinaryContentCreateRequest(
    @NotBlank
    String fileName,

    @NotBlank
    String contentType,

    @NotNull
    @Size(min = 1)
    byte[] bytes
) {

}
