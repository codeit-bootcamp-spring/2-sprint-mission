package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record BinaryContentCreateRequest(

    @NotBlank(message = "File 이름 입력 필수")
    @Size(max = 255, message = "File이름은 255자 이하")
    String fileName,

    @NotBlank(message = "Content 타입 입력 필수")
    @Size(max = 100, message = "Content Type은 100자 이하")
    String contentType,

    // 업로드된 파일 데이터 자체가 저장되어있는 공
    @NotEmpty(message = "File binary data 입력 필수")
    byte[] bytes
) {

}
