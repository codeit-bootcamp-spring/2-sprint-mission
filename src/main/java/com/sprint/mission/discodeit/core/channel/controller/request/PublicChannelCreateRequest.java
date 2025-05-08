package com.sprint.mission.discodeit.core.channel.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Public Channel Create")
public record PublicChannelCreateRequest(
    @Schema(description = "Channel username", example = "string")
    @NotBlank @Size(min = 1, max = 50) String name,
    @Schema(description = "Channel newDescription", example = "string")
    @Size(max = 50) String description
) {

}
