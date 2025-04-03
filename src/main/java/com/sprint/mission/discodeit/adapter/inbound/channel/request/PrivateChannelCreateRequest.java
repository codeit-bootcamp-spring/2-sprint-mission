package com.sprint.mission.discodeit.adapter.inbound.channel.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(description = "Private Channel Create")
public record PrivateChannelCreateRequest(
    @Schema(
        description = "사용자 ID 목록",
        example = "[\"3fa85f64-5717-4562-b3fc-2c963f66afa6\"]"
    )
    List<UUID> participantIds
) {

}
