package com.sprint.mission.discodeit.dto.request;

import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record PrivateChannelCreateRequest(
    @NotNull(message = "참여자 ID는 필수 입력 사항입니다.")
    List<UUID> participantIds,
    @NotNull(message = "채널 이름은 필수 입력 사항입니다.")
    String channelName
) {

}
