package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelUpdateRequest(
    @NotBlank(message = "수정할 채널명을 입력해주세요")
    String newName,
    String newDescription
) {

}
