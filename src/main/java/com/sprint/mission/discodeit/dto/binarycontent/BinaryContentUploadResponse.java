package com.sprint.mission.discodeit.dto.binarycontent;

import java.util.UUID;

public record BinaryContentUploadResponse(
        UUID fileId,
        String filePath,
        String fileName,
        String contentType,
        long fileSize
){
}
