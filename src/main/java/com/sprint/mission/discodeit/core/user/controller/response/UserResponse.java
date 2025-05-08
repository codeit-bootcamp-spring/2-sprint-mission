package com.sprint.mission.discodeit.core.user.controller.response;

import com.sprint.mission.discodeit.core.content.controller.response.BinaryContentResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "User 성공적으로 생성됨")
public record UserResponse(
    @Schema(description = "User Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,
    @Schema(description = "User 이름", example = "string")
    String username,
    @Schema(description = "User 이메일", example = "string")
    String email,
    @Schema(description = "User 프로필")
    BinaryContentResponse profile,
    @Schema(description = "온라인 상태", example = "true")
    boolean online
) {


}
