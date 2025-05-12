package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
    @Size(max = 50, message = "채널 이름은 50자를 초과할 수 없습니다.")
    String newName,

    @Size(max = 100, message = "채널 설명은 100자를 초과할 수 없습니다.")
    String newDescription
) {

}
