package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(
    @NotBlank(message = "채널 이름은 필수입니다.")
    @Size(min = 3, max = 50, message = "채널 이름은 3자 이상, 50자 이하로 입력해야 합니다.")
    String name,

    @Size(max = 500, message = "채널 설명은 500자 이하로 입력해야 합니다.")
    String description
) {

}
