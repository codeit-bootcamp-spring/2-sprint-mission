package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateResponse(
        ChannelType type,
        UUID id,
        List<User> users,
        List<ReadStatus> readStatuses
) {}
