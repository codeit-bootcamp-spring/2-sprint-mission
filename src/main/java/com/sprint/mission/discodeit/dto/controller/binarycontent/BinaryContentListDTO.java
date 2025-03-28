package com.sprint.mission.discodeit.dto.controller.binarycontent;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record BinaryContentListDTO(
        List<BinaryContentDTO> binaryContentDTOList
) {
}
