package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    @NotEmpty(message = "참여자 목록은 최소 1명 이상이어야 합니다.")
    List<UUID> participantIds
) {

}
