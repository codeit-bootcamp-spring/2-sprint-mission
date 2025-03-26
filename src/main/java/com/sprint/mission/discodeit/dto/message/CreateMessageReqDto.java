package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record CreateMessageReqDto(
        UUID userId,
        UUID channelId,
        String content,
        List<byte[]> fileDatas
) {

}
