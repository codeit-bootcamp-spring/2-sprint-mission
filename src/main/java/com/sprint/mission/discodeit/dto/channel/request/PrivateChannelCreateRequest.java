package com.sprint.mission.discodeit.dto.channel.request;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public record PrivateChannelCreateRequest(
        List<User> users
) {}
