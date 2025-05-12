package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    @NotNull(message = "참가자 목록은 필수입니다.")
    @NotEmpty(message = "참가자 목록은 비어있을 수 없습니다.")
    List<UUID> participantIds
) {

}
