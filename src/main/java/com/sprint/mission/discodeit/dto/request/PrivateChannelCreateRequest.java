package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Schema(description = "비공개 채널 생성 요청")
public record PrivateChannelCreateRequest(
        @NotEmpty(message = "참여자 ID 목록은 비어 있을 수 없습니다.")
        List<@NotNull(message = "참여자 ID는 필수입니다.") UUID> participantIds
) {
}
