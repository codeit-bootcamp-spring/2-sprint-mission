package com.sprint.mission.discodeit.adapter.inbound.user.dto;

import java.util.List;

public record UserDisplayList(
        List<UserFindDTO> users
) {
}
