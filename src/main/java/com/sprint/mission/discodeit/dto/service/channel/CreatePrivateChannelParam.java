package com.sprint.mission.discodeit.dto.service.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public record CreatePrivateChannelParam(
    List<UUID> participantIds
) {

}
