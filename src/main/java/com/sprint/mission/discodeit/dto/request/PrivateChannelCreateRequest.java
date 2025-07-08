package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Schema(description = "개인 채널 생성 요청")
public record PrivateChannelCreateRequest(
    @Schema(description = "참여자 ID 목록 (정확히 2명)", example = "[\"df69d82b-a0ff-4c9b-a625-988da6f1c08b\", \"other-user-uuid\"]")
    @NotNull(message = "참여자 ID는 필수 입력 사항입니다.")
    @Size(min = 2, max = 2, message = "개인 채널은 정확히 2명의 참여자가 필요합니다.")
    List<UUID> participantIds
) {

}
