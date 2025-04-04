package com.sprint.mission.discodeit.adapter.inbound.user.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User Update Request")
public record UserUpdateRequest(
    @Schema(description = "User new Name", example = "string")
    String newName,

    @Schema(description = "User new Email", example = "string")
    String newEmail,

    @Schema(description = "User new Password", example = "string")
    String newPassword

) {

}
