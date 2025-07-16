package com.sprint.mission.discodeit.dto.controller.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FindBinaryContentResponseDTO(
    UUID id,
    String filename,
    long size,
    String contentType,
    BinaryContentUploadStatus uploadStatus
) {

}
