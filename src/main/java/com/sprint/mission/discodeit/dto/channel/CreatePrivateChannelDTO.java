package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public record CreatePrivateChannelDTO(
        List<User> userlist
) {
}
