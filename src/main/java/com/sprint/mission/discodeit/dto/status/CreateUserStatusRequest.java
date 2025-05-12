package com.sprint.mission.discodeit.dto.status;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateUserStatusRequest(
    @NotNull(message = "사용자 ID는 필수입니다.")
    UUID userId
) {

}
