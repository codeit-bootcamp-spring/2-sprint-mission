package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "바이너리 콘텐츠 응답 DTO")
public record BinaryContentDto(

        @Schema(description = "바이너리 콘텐츠 ID")
        java.util.UUID id,

        @Schema(description = "파일명")
        String fileName,

        @Schema(description = "파일 크기")
        Long size,

        @Schema(description = "콘텐츠 타입")
        String contentType,

        @Schema(description = "바이너리 데이터")
        byte[] bytes
) {}
