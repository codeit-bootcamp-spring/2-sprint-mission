package com.sprint.mission.discodeit.dto.service.channel;

import java.util.UUID;

public record UpdateChannelParam(
    String newName,
    String newDescription
) {

}
