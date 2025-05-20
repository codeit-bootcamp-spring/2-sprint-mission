package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "바이너리 콘텐츠 생성 요청")
public record BinaryContentCreateRequest(
        @NotBlank(message = "파일명은 필수입니다.")
        String fileName,

        @NotBlank(message = "콘텐츠 타입은 필수입니다.")
        String contentType,

        @NotNull(message = "파일 데이터는 필수입니다.")
        @Size(min = 1, message = "파일 데이터는 비어 있을 수 없습니다.")
        byte[] bytes
) {
}
