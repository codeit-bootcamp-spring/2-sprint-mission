package com.sprint.mission.discodeit.service.dto.binarycontentdto;

import java.nio.file.Path;
import java.util.UUID;

public record BinaryContentUpdateDto(
        UUID Id,
        Path newProfilePath

) {

}
