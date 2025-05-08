package com.sprint.mission.discodeit.adapter.inbound.channel.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Channel Update")
public record ChannelUpdateRequest(
    @Schema(description = "Channel New username", example = "string")
    @Size(max = 50) String newName,
    @Schema(description = "Channel New newDescription", example = "string")
    @Size(max = 50) String newDescription
) {

}
