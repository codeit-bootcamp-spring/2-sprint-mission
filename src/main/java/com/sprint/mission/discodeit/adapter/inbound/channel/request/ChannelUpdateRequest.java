package com.sprint.mission.discodeit.adapter.inbound.channel.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Channel Update")
public record ChannelUpdateRequest(
    @Schema(description = "Channel New name", example = "string")
    String newName,
    @Schema(description = "Channel New description", example = "string")
    String newDescription
) {

}
