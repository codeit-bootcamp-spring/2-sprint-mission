package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

public record BinaryContentCreateRequest(
    @NotNull String fileName,
    String contentType,
    byte[] bytes
) {

}
