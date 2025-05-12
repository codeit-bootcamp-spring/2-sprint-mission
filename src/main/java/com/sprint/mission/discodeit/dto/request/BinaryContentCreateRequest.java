package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BinaryContentCreateRequest(
    @NotBlank(message = "파일 이름은 비어 있을 수, 공백일 수 없습니다.")
    String fileName,

    @NotBlank(message = "ContentType은 비어 있을 수, 공백일 수 없습니다.")
    String contentType,

    @NotNull(message = "파일 내용은 필수 입력 값입니다.")
    @Size(min = 1, message = "파일 내용은 최소 1바이트 이상이어야 합니다.")
    byte[] bytes
) {

}
