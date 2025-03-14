package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserReadResponse;

import java.util.List;

public record PrivateChannelCreateRequest(
        List<UserReadResponse> users
) {
}
