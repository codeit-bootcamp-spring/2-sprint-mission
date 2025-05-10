package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
    @NotBlank(message = "채널 이름 입력 필수")
    @Size(max = 100, message = "채널 이름은 100자 이하")
    String newName,

    @NotBlank(message = "채널 설명 입력 필수")
    @Size(max = 500, message = "채널 설명은 500자 이하")
    String newDescription
) {

}
