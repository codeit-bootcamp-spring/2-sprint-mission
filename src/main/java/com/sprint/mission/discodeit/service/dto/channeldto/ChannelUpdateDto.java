package com.sprint.mission.discodeit.service.dto.channeldto;

import java.nio.file.Path;
import java.util.UUID;

public record   ChannelUpdateDto(
        UUID channelId,
        String changeChannel,
        String changeDescription
) {

}
