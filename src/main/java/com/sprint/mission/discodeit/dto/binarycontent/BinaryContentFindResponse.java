package com.sprint.mission.discodeit.dto.binarycontent;

import java.util.UUID;

public record BinaryContentFindResponse(
        UUID binaryId,
        String filePath,
        String fileName,
        String contentType,
        long fileSize,
        String bytes        // base64로 인코딩된 이미지 바이트
) {
}
