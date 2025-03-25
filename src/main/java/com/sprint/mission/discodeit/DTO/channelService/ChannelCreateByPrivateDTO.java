package com.sprint.mission.discodeit.DTO.channelService;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public record ChannelCreateByPrivateDTO(
        List<User> users
) {
}
