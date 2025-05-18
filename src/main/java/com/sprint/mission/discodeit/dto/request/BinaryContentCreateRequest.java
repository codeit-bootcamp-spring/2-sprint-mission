package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BinaryContentCreateRequest(

    @NotBlank(message = "파일 이름 비어 있을 수 없습니다.")
    @Size(max = 255, message = "파일 이름은 255자를 넘을 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "파일 이름에 허용되지 않는 문자가 포함되어 있습니다.")
    String fileName,

    @NotBlank(message = "컨텐츠 타입은 비어 있을 수 없습니다.")
    @Pattern(regexp = "^[-\\w]+/[-\\w]+$", message = "유효한 MIME 타입이 아닙니다.")
    String contentType,

    @NotNull
    byte[] bytes
) {

}
