package com.sprint.mission.discodeit.dto.controller.binarycontent;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record FindAllBinaryContentInResponseDTO(
        @NotNull
        @Size(min = 1)
        List<UUID> attachmentIds
) {
}
