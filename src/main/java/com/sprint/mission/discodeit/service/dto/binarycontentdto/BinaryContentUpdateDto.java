package com.sprint.mission.discodeit.service.dto.binarycontentdto;

import java.util.UUID;

public record BinaryContentUpdateDto(
        UUID Id,
        String newFileName,
        String newContentType,
        byte[] newBytes

) {

}
