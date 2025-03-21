package com.sprint.mission.discodeit.dto.display;

import com.sprint.mission.discodeit.dto.UserFindDTO;

import java.util.List;

public record UserDisplayList(
        List<UserFindDTO> users
) {
}
