package com.sprint.mission.discodeit.dto.message;

import java.util.List;

public record UpdateMessageReqDto(
        String content,
        List<byte[]> fileDatas
) {
}
