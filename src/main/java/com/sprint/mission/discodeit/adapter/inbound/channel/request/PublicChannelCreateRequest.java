package com.sprint.mission.discodeit.adapter.inbound.channel.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Public Channel Create")
public record PublicChannelCreateRequest(
    @Schema(description = "Channel name", example = "string")
    @NotBlank String name,
    @Schema(description = "Channel description", example = "string")
    String description
) {

}
