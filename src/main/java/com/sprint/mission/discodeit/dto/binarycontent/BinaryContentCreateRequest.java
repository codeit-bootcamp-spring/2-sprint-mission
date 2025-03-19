package com.sprint.mission.discodeit.dto.binarycontent;

public record BinaryContentCreateRequest(
    String filePath,
    String fileName,
    String fileType,
    long fileSize
) {
}
