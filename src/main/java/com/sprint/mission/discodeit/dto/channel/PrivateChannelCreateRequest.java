package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    @NotNull(message = "참여자 목록이 필요합니다.")
    @Size(min = 1, message = "1명 이상의 참여자가 필요합니다.")
    List<UUID> participantIds
) {

}
