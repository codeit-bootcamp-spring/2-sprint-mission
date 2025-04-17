package com.sprint.mission.discodeit.service.dto.request.binarycontentdto;

import java.util.UUID;

public record BinaryContentUpdateDto(
        UUID Id,
        String newFileName,
        String newContentType,
        byte[] newBytes

) {

}
