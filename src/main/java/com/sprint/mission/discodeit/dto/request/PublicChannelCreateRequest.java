package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest(

    @NotBlank(message = "채널 이름은 비울 수 없습니다.")
    String name,
    String description
) {

}
