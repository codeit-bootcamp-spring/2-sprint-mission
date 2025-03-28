package com.sprint.mission.discodeit.dto.controller.binarycontent;

import java.util.List;
import java.util.UUID;

public record FindAllBinaryContenInResponseDTO(
        List<UUID> attachmentIds
) {
}
