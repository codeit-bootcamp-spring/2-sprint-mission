package com.sprint.mission.discodeit.adapter.inbound.message.response;

import com.sprint.mission.discodeit.adapter.inbound.content.response.BinaryContentResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Message 성공적으로 생성됨")
public record MessageResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UserResponse author,
    @Schema(
        description = "Binary Content ID 목록"
    )
    List<BinaryContentResponse> attachment
) {

}
