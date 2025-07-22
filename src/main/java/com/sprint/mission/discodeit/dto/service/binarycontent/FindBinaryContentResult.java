package com.sprint.mission.discodeit.dto.service.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import java.util.UUID;

public record FindBinaryContentResult(
    UUID id,
    String filename,
    long size,
    String contentType,
    BinaryContentUploadStatus uploadStatus
) {

}
