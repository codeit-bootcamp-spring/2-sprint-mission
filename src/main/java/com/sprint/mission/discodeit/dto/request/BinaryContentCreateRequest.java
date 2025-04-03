package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "바이너리 콘텐츠 생성 요청")
public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
