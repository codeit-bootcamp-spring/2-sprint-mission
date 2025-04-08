package com.sprint.mission.discodeit.service.dto.channeldto;

import java.util.List;
import java.util.UUID;

public record ChannelCreatePrivateDto(

        List<UUID> participantIds

) {
}
