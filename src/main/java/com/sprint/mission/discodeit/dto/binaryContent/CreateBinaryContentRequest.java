package com.sprint.mission.discodeit.dto.binaryContent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateBinaryContentRequest(

    @NotBlank
    String fileName,

    @NotNull
    @PositiveOrZero
    Long size,

    @NotNull
    @Size(max = 100)
    String contentType,

    @NotNull
    byte[] bytes
) {

}