package com.sprint.mission.discodeit.core.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "User Update Request")
public record UserUpdateRequest(
    @Schema(description = "User new Name", example = "string")
    @Size(max = 20) String newUsername,

    @Schema(description = "User new Email", example = "string")
    @Email String newEmail,

    @Schema(description = "User new Password", example = "string")
    @Size(max = 20) String newPassword

) {

}
