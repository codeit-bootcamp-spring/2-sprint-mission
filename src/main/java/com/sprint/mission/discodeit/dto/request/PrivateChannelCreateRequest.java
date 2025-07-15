package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
        @NotNull(message = "참여자 ID는 필수 입력 사항입니다.")
        @Size(min = 2, max = 2, message = "개인 채널은 정확히 2명의 참여자가 필요합니다.")
        List<UUID> participantIds
) {

}