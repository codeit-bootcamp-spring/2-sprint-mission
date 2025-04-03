package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Schema(description = "Private Channel 생성 정보")
public record PrivateChannelCreateRequest(
    @NotNull(message = "participantsIds(List)에는 null이 들어갈 수 없습니다.")
    List<UUID> participantIds
) {

}
