package com.sprint.mission.discodeit.dto.Channel;

import java.util.List;
import java.util.UUID;

public record CreatePrivateChannelDto(
        List<UUID> userIds
) {}
